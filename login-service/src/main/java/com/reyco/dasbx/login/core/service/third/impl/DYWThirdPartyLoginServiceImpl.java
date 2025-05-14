package com.reyco.dasbx.login.core.service.third.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
import com.reyco.dasbx.login.core.utils.HttpClient;
import com.reyco.dasbx.login.core.utils.HttpClient.HttpResult;
import com.reyco.dasbx.model.constants.AccountType;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.rabbitmq.service.RabbitProducrService;

@Service("dyw")
public class DYWThirdPartyLoginServiceImpl implements ThirdPartyLoginService{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ThirdConfig thirdConfig;
	@Autowired
	private AccountFeignClientService accountFeignClientService;
	@Autowired
	private RabbitProducrService rabbitProducrService;
	
	@Override
	public Boolean supports(AccountType accountType) {
		return AccountType.ACCOUNT_TYPE_BACK==accountType || AccountType.ACCOUNT_TYPE_PORTAL==accountType || AccountType.ACCOUNT_TYPE_OPEN==accountType;
	}
	@Override
	public String login(String type) throws Exception {
		Party party = thirdConfig.getParty().get(type);
		String authorizeUrl = party.getAuthorizeUrl();
		String callbackUrl = party.getCallbackUrl();
		String clientId = party.getClientId();
		String state = UUID.randomUUID().toString().replaceAll("-", "");
		String url=authorizeUrl
				+ "?client_id="+clientId
				+ "&response_type=code"
				+ "&redirect_uri="+URLEncoder.encode(callbackUrl)
				+ "&state="+state;
		return url;
	}

	@Override
	public SysAccountToken callback(String type,String code, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject accessTokenJSONObject = getAccessToken(type,code);
		String accessToken = accessTokenJSONObject.getString("access_token");
		JSONObject userInfo = getUserInfo(type,accessToken);
		String uid = userInfo.getString("uid");
		String nickname = userInfo.getString("nickname");
		SysAccount account = accountFeignClientService.getByUid(uid);
		if(account==null) {
			AccountRegisterMessage accountRegisterMessage = new AccountRegisterMessage();
			accountRegisterMessage.setUid(uid);
			accountRegisterMessage.setNickname(nickname);
			accountRegisterMessage.setType(AccountType.getAccountType(type).getType());
			account = Convert.sourceToTarget(accountRegisterMessage, SysAccount.class);
			rabbitProducrService.send(RabbitConstants.ACCOUNT_EXCHANGE, RabbitConstants.ACCOUNT_REGISTER_ROUTE_KEY,accountRegisterMessage);
		}
		String ip = CusAccessObjectUtil.getIpAddress(request);
		String cityName = IPDataUtils.getCityName(ip); 
		SysAccountToken accountToken = Convert.sourceToTarget(account, SysAccountToken.class);
		accountToken.setIp(ip);
		accountToken.setCity(cityName);
		TokenUtils.createToken(accountToken);
		CookieUtil.setCookie(request, response, Constants.TOKEN_NAME, accountToken.getToken(), -1);
		Party party = thirdConfig.getParty().get(type);
		return accountToken;
	}
	/**
	 * 获取token信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	private JSONObject getAccessToken(String type,String code) throws Exception {
		Party party = thirdConfig.getParty().get(type);
		String clientId = party.getClientId();
		String clientSecret = party.getClientSecret();
		String callbackURL = party.getCallbackUrl();
		String tokenUrl = party.getTokenUrl();
		String grant_type = "authorization_code";
		tokenUrl = tokenUrl+"?grant_type="+grant_type+ "&client_id="
				+ clientId + "&client_secret=" + clientSecret + "&redirect_uri=" + callbackURL + "&code=" + code;
		/*HttpEntity<Object> httpEntity = new HttpEntity<Object>(null,getHeader());
		String tokenObject = restTemplate.getForObject(tokenUrl, String.class, httpEntity);
		JSONObject dywTokenObject =JSONObject.parseObject(tokenObject);*/
		HttpResult httpResult = HttpClient.httpGet(tokenUrl, getHeader1(), null);
		String tokenObject = httpResult.getContent();
		JSONObject dywTokenObject =JSONObject.parseObject(tokenObject);
		logger.info("dyw."+dywTokenObject.toString());
		return dywTokenObject;
	}
	/**
	 * 获取用户信息
	 * @param token
	 * @return
	 * @throws Exception
	 */
	private JSONObject getUserInfo(String type,String token) throws Exception {
		Party party = thirdConfig.getParty().get(type);
		String infoUrl = party.getInfoUrl();
		String get_user_info_url=infoUrl+"?access_token="+token;
		/*HttpHeaders header = getHeader();
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(null,header);
		String userInfoObject = restTemplate.getForObject(get_user_info_url,String.class,httpEntity);
		JSONObject dywUserInfoObject = JSONObject.parseObject(userInfoObject);*/
		HttpResult httpResult = HttpClient.httpGet(get_user_info_url, getHeader1(), null);
		String userInfoObject = httpResult.getContent();
		JSONObject dywUserInfoObject =JSONObject.parseObject(userInfoObject);
		logger.info("dyw."+dywUserInfoObject.toString());
		return dywUserInfoObject;
	}
	private HttpHeaders getHeader(){
		HttpHeaders headers = new HttpHeaders();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(Constants.ACCEPT_NAME, "true");
        headers.set(Constants.CODE_NAME, request.getHeader(Constants.CODE_NAME));
        headers.set(Constants.TOKEN_NAME, request.getHeader(Constants.TOKEN_NAME));
        headers.set("Cookie",request.getHeader("Cookie"));
        return headers;
	}
	private List<String> getHeader1(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		List<String> headers = new ArrayList<>();
		headers.add(Constants.ACCEPT_NAME+"=true");
		headers.add(Constants.CODE_NAME+"="+request.getHeader(Constants.CODE_NAME));
		headers.add(Constants.TOKEN_NAME+"="+request.getHeader(Constants.TOKEN_NAME));
		headers.add("Cookie="+request.getHeader("Cookie"));
		return headers;
	}
}
