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
import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.commons.utils.net.CookieUtil;
import com.reyco.dasbx.commons.utils.net.CusAccessObjectUtil;
import com.reyco.dasbx.commons.utils.net.IPDataUtils;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.login.core.config.Party;
import com.reyco.dasbx.login.core.config.ThirdConfig;
import com.reyco.dasbx.login.core.feign.AccountFeignClientService;
import com.reyco.dasbx.login.core.service.third.ThirdPartyLoginService;
import com.reyco.dasbx.model.constants.AccountType;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.vo.SysAccountToken;


@Component("baidu")
public class BaiduThirdPartyLoginServiceImpl implements ThirdPartyLoginService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ThirdConfig thirdConfig;
	@Autowired
	private AccountFeignClientService accountFeignClientService;
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public Boolean supports(AccountType accountType) {
		return AccountType.ACCOUNT_TYPE_BAIDU==accountType;
	}
	
	@Override
	public String login(String type) {
		Party party = thirdConfig.getParty().get(type);
		String authorizeUrl = party.getAuthorizeUrl();
		String backUrl = party.getCallbackUrl();
		String clientId = party.getClientId();
		String state = UUID.randomUUID().toString().replaceAll("-", "");
		String url = authorizeUrl+"?client_id="+ clientId +"&response_type=code&display=pc"
				+ "&redirect_uri="+URLEncoder.encode(backUrl) + "&state="+state;
		return url;
	}

	@Override
	public SysAccountToken callback(String type,String code,HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONObject accessToken = getAccessToken(type,code);
		String token = accessToken.getString("access_token");
		JSONObject userInfo = getUserInfo(type,token);
		// 获取登录ip
		String ip = CusAccessObjectUtil.getIpAddress(request);
		String cityName = IPDataUtils.getCityName(ip);
		String username = userInfo.getString("username");
		SysAccount account = accountFeignClientService.getByUsername(username);
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
		String grantType = "authorization_code";
		Party party = thirdConfig.getParty().get(type);
		String clientId = party.getClientId();
		String clientSecret = party.getClientSecret();
		String callbackUrl = party.getCallbackUrl();
		String token_url = party.getTokenUrl();
		String get_token_url = token_url+"?" + grantType + "&client_id="+ clientId 
				+ "&client_secret=" + clientSecret + "&redirect_uri=" + callbackUrl + "&code=" + code;;
		 String response = restTemplate.getForObject(get_token_url,String.class);
		JSONObject baiduTokenObject = JSONObject.parseObject(response);
		logger.info("baidu"+baiduTokenObject.toString());
		return baiduTokenObject;
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
		JSONObject baiduUserInfo = JSONObject.parseObject(body);
		logger.info("baidu_userInfo"+baiduUserInfo.toString());
		return baiduUserInfo;
	}
}
