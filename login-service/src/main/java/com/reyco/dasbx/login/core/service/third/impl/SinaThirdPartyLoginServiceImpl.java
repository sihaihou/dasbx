package com.reyco.dasbx.login.core.service.third.impl;

import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.CookieUtil;
import com.reyco.dasbx.commons.utils.CusAccessObjectUtil;
import com.reyco.dasbx.commons.utils.IPDataUtils;
import com.reyco.dasbx.config.rabbitmq.service.RabbitProducrService;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.login.core.config.Party;
import com.reyco.dasbx.login.core.config.ThirdConfig;
import com.reyco.dasbx.login.core.feign.AccountFeignClientService;
import com.reyco.dasbx.login.core.service.third.ThirdPartyLoginService;
import com.reyco.dasbx.model.constants.AccountType;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.msg.AccountRegisterMessage;
import com.reyco.dasbx.model.vo.SysAccountToken;

@Component("weibo")
public class SinaThirdPartyLoginServiceImpl implements ThirdPartyLoginService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String KEY = "sina";
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
		return AccountType.ACCOUNT_TYPE_WEIBO==accountType;
	}
	
	@Override
	public String login(String type) {
		Party party = thirdConfig.getParty().get(KEY);
		String authorizeUrl = party.getAuthorizeUrl();
		String callbackUrl = party.getCallbackUrl();
		String clientId = party.getClientId();
		String state = UUID.randomUUID().toString().replaceAll("-", "");
		String url = authorizeUrl+"?client_id="+clientId+"&response_type=code"+"&scope=all"
				+ "&display=pc"+"&redirect_uri="+URLEncoder.encode(callbackUrl)+"&state="+state;
		return url;
	}

	@Override
	public SysAccountToken callback(String type,String code,HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONObject accessTokenObj = getAccessToken(type,code);
		String accessToken = accessTokenObj.getString("access_token");
		JSONObject userInfo = getUserInfo(type,accessToken);
		String uid = userInfo.getString("uid");
		SysAccount account = accountFeignClientService.getByUsername(uid);
		if(account==null) {
			AccountRegisterMessage accountRegisterMessage = new AccountRegisterMessage();
			accountRegisterMessage.setUid(uid);
			accountRegisterMessage.setNickname(uid);
			accountRegisterMessage.setType(AccountType.ACCOUNT_TYPE_WEIBO.getType());
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
		String grant_type = "authorization_code";
		String clientId = party.getClientId();
		String clientSecret = party.getClientSecret();
		String callbackURL = party.getCallbackUrl();
		String tokenUrl = party.getTokenUrl();
		LinkedMultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("grant_type", grant_type);
		requestEntity.add("client_id", clientId);
		requestEntity.add("client_secret", clientSecret);
		requestEntity.add("redirect_uri", callbackURL);
		requestEntity.add("code", code);
		ResponseEntity<String> postForEntity = restTemplate.postForEntity(callbackURL, requestEntity, String.class);
		String body = postForEntity.getBody();
		JSONObject wbTokenObject = JSONObject.parseObject(body);
		logger.info("weibo."+wbTokenObject.toString());
		return wbTokenObject;
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
		LinkedMultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("access_token", token);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(infoUrl, requestEntity, String.class);
		String body = responseEntity.getBody();
		JSONObject wbUserInfoObject = JSONObject.parseObject(body);
		logger.info("weibo."+wbUserInfoObject.toString());
		return wbUserInfoObject;
	}
}
