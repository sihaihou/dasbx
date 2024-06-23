package com.reyco.dasbx.login.core.service.third.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reyco.dasbx.login.core.service.third.ThirdPartyLoginService;
import com.reyco.dasbx.model.constants.AccountType;
import com.reyco.dasbx.model.vo.SysAccountToken;

@Component
public class ThirdPartyLoginServiceContext{

	@Autowired
	private List<ThirdPartyLoginService> thirdPartyLoginServices;

	public String login(String type) throws Exception {
		ThirdPartyLoginService thirdPartyLoginService = getMatchThirdPartyLoginService(AccountType.getAccountType(type));
		return thirdPartyLoginService.login(type);
	}

	public SysAccountToken callback(String type,String code, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ThirdPartyLoginService thirdPartyLoginService = getMatchThirdPartyLoginService(AccountType.getAccountType(type));
		return thirdPartyLoginService.callback(type,code, request, response);
	}
	private ThirdPartyLoginService getMatchThirdPartyLoginService(AccountType accountType) {
		for (ThirdPartyLoginService thirdPartyLoginService : thirdPartyLoginServices) {
			if(thirdPartyLoginService.supports(accountType)) {
				return thirdPartyLoginService;
			}
		}
		return null;
	}
}
