package com.reyco.dasbx.login.core.service;

import javax.servlet.http.HttpServletRequest;

import com.reyco.dasbx.config.exception.core.ArgumentException;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.login.core.model.dto.EmailLoginDto;
import com.reyco.dasbx.model.vo.SysAccountToken;

public interface LoginService {
	
	/**
	 * 密码登录
	 * @param t
	 * @return
	 * @throws ArgumentException
	 * @throws AuthenticationException
	 */
	SysAccountToken login(String t) throws ArgumentException,AuthenticationException;
	
	/**
	 * 创建EmailCode
	 * @param email
	 * @return
	 */
	void createEmailCode(String email);
	/**
	 * 邮箱登录
	 * @param t
	 * @return
	 * @throws ArgumentException
	 * @throws AuthenticationException
	 */
	SysAccountToken emailLogin(EmailLoginDto emailLoginDto) throws ArgumentException,AuthenticationException;
	
	Boolean isLogin() throws ArgumentException,AuthenticationException;
	
	void logout(HttpServletRequest request) throws AuthenticationException ;
	
}
