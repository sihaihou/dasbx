package com.reyco.dasbx.login.core.service.third.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.commons.utils.net.CookieUtil;
import com.reyco.dasbx.commons.utils.net.CusAccessObjectUtil;
import com.reyco.dasbx.commons.utils.net.IPDataUtils;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.login.core.config.Party;
import com.reyco.dasbx.login.core.config.ThirdConfig;
import com.reyco.dasbx.login.core.feign.AccountFeignClientService;
import com.reyco.dasbx.login.core.model.msg.AccountRegisterMessage;
import com.reyco.dasbx.login.core.service.third.ThirdPartyLoginService;
import com.reyco.dasbx.model.constants.AccountType;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.rabbitmq.service.RabbitProducrService;

@Component("qq")
public class QQThirdPartyLoginServiceImpl implements ThirdPartyLoginService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String KEY = "qq";
	
	@Autowired
	private ThirdConfig thirdConfig;
	@Autowired
	private AccountFeignClientService accountFeignClientService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private RabbitProducrService RabbitProducrService;
	@Override
	public Boolean supports(AccountType accountType) {
		return AccountType.ACCOUNT_TYPE_QQ==accountType;
	}
	
	@Override
	public String login(String type) {
		Party party = thirdConfig.getParty().get(type);
		String authorizeUrl = party.getAuthorizeUrl();
		String backUrl = party.getCallbackUrl();
		String clientId = party.getClientId();
		String state = UUID.randomUUID().toString().replaceAll("-", "");
		String url = authorizeUrl+"?client_id=" + clientId + "&response_type=code" + "&scope=all"
				+ "&redirect_uri=" + URLEncoder.encode(backUrl) + "&state="+state;
		return url;
	}

	@Override
	public SysAccountToken callback(String type,String code,HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取token信息
		Map<String, String> map = getAccessToken(type,code);
		String accessToken = map.get("access_token");
		// 获取appid
		JSONObject openidObj = getAppid(type,accessToken);
		String appid = openidObj.getString("client_id");
		String openid = openidObj.getString("openid");
		// 获取用户信息
		JSONObject userInfo = getUserInfo(accessToken,appid,openid);
		SysAccount account = accountFeignClientService.getByUsername(openid);
		if(account==null) {
			AccountRegisterMessage accountRegisterMessage = new AccountRegisterMessage();
			accountRegisterMessage.setUid(openid);
			accountRegisterMessage.setNickname(userInfo.getString("nickname"));
			accountRegisterMessage.setGender(userInfo.getString("gender").equals("女")?(byte)0:1);
			accountRegisterMessage.setType(AccountType.ACCOUNT_TYPE_QQ.getType());
			account = Convert.sourceToTarget(accountRegisterMessage, SysAccount.class);
			RabbitProducrService.send(RabbitConstants.ACCOUNT_EXCHANGE, RabbitConstants.ACCOUNT_REGISTER_ROUTE_KEY,accountRegisterMessage);
		}
		// 获取登录用户
		String ip = CusAccessObjectUtil.getIpAddress(request);
		String cityName = IPDataUtils.getCityName(ip); 
		SysAccountToken accountToken = Convert.sourceToTarget(account, SysAccountToken.class);
		accountToken.setIp(ip);
		accountToken.setCity(cityName);
		TokenUtils.createToken(accountToken);
		CookieUtil.setCookie(request, response, Constants.TOKEN_NAME, accountToken.getToken(), -1);
		Party party = thirdConfig.getParty().get(KEY);
		return accountToken;
	}
	/**
	 * 获取token信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	private Map<String,String> getAccessToken(String type,String code) throws Exception {
		Party party = thirdConfig.getParty().get(type);
		Map<String,String> map = new HashMap<>();
		String tokenUrl = party.getTokenUrl();
		String clientId = party.getClientId();
		String clientSecret = party.getClientSecret();
		String backURL = party.getCallbackUrl();
		String grant_type = "authorization_code";
		String get_token_url = tokenUrl+"?grant_type="+grant_type+"&client_id="+clientId
				+"&client_secret="+clientSecret+"&redirect_uri="+backURL+"&code="+code;
		String response = restTemplate.getForObject(get_token_url,String.class);
		logger.info("token:"+response);
		String[] responses =  response.split("&");
		map.put("access_token", getValue(responses,0));
		map.put("expires_in", getValue(responses,1));
		map.put("refresh_token", getValue(responses,2));
		return map;
	}
	private  String getValue(String[] response,Integer index) {
		String values = response[index];
		String[] value = values.split("=");
		return value[1];
	}
	/**
	 * 获取appid
	 * @return
	 * @throws JSONException 
	 */
	private JSONObject getAppid(String type,String token) throws JSONException {
		String get_openid_url = "https://graph.qq.com/oauth2.0/me?access_token="+token;
		String response = restTemplate.getForObject(get_openid_url,String.class);
		logger.info("openId"+response);
		String data = response.substring(response.indexOf("{"), response.indexOf("}")+1);
		JSONObject jsonObject = JSONObject.parseObject(response);
		return jsonObject;
	}
	/**
	 * 获取用户信息
	 * @param token
	 * @return
	 * @throws Exception
	 */
	private JSONObject getUserInfo(String token,String appid,String openid) throws Exception {
		Party party = thirdConfig.getParty().get(KEY);
		String infoUrl = party.getInfoUrl();
		String userInfo_url = infoUrl+"?access_token="+token+"&appid="+appid+"&openid="+openid;
		String response = restTemplate.getForObject(userInfo_url,String.class);
		logger.info("userInfo:"+response);
		JSONObject jsonObject = JSONObject.parseObject(response);
		return jsonObject;
	}
}
