package com.reyco.dasbx.login.core.service.third;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reyco.dasbx.model.constants.AccountType;
import com.reyco.dasbx.model.vo.SysAccountToken;

/**
 * 第三方登录顶级接口
 * @author reyco
 * @date 2019年7月23日
 *
 */
public interface ThirdPartyLoginService {
	/**
	 * 是否支持
	 * @param type
	 * @return
	 */
	Boolean supports(AccountType accountType);
	/**
	 * 登录 ---返回重定向地址
	 * @return
	 */
	String login(String type)throws Exception;
	/**
	 * 回调地址
	 * @param code         登录成功后返回的code
	 * @param request      参数request
	 * @return
	 * @throws Exception
	 */
	SysAccountToken callback(String type,String code,HttpServletRequest request,HttpServletResponse response) throws Exception;
}
