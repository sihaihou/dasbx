package com.reyco.dasbx.login.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reyco.dasbx.commons.utils.encrypt.SimpleHash;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.login.core.feign.AccountFeignClientService;
import com.reyco.dasbx.login.core.service.authentication.AbstractAuthenticating;
import com.reyco.dasbx.login.core.service.authentication.AuthenticationInfo;
import com.reyco.dasbx.login.core.service.authentication.AuthenticationToken;
import com.reyco.dasbx.login.core.service.authentication.SaltAuthenticationInfo;
import com.reyco.dasbx.model.domain.SysAccount;

/**
 * 认证实现
 * @author reyco
 *
 */
@Component
public class DefaultAuthenticating extends AbstractAuthenticating{
	
	@Autowired
	private AccountFeignClientService accountFeignClientService;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = token.getPrincipal().toString();
		SysAccount account = accountFeignClientService.getByUsername(username);
		if(account==null) {
			log.error("用户名不存在！");
			throw new AuthenticationException("用户名或密码错误！");
		}
		AuthenticationInfo authenticationInfo = new SaltAuthenticationInfo(account,account.getPassword(),SimpleHash.SHA_256,account.getSalt(),2);
		return authenticationInfo;
	}

}
