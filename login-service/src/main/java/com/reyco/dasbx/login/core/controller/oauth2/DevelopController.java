package com.reyco.dasbx.login.core.controller.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.login.core.service.oauth2.Oauth2Service;

/**y
 * 模拟第三方登录开发接口
 * @author reyco
 * @date 2019年7月26日
 *
 */
@Controller
@RequestMapping("oauth2")
public class DevelopController {
	
	@Autowired
	private Oauth2Service oauth2Service;
	
	/**
	 * 重定向授权登录页
	 * @param client_id       	id
	 * @param response_type		响应类型默认code
	 * @param redirect_uri		第三方回调地址
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("authorize")
	public void authorize(HttpServletResponse response,HttpServletRequest request,String client_id,String redirect_uri,String response_type) throws Exception {
		oauth2Service.authorize(client_id, redirect_uri,response_type);
		request.getRequestDispatcher("../third/login.html").forward(request, response);
	}
	/**
	 * 验证登录，生成token
	 * @param username 		用户名
	 * @param password 		密码
	 * @param redirect_uri  第三方回调地址(建议前端放在请求头中)
	 * @return				
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("login")
	public Object login(String t,String redirect_uri) throws Exception {
		JSONObject token = oauth2Service.createToken(t);
		String code = token.getString("token");
		if(!redirect_uri.contains("?")) {
			redirect_uri = redirect_uri+"?code="+code;
		}else {
			redirect_uri = redirect_uri+"&code="+code;
		}
		return R.success(redirect_uri);
	}
	/**
	 * 获取token信息
	 * @param code				登录验证成功返回的code
	 * @param client_id			id
	 * @param client_secret		key
	 * @param grant_type		默认authorization_code
	 * @param redirect_uri		第三方回调地址
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("accessToken")
	public Object access_token(String code,String client_id,String client_secret,String grant_type,String redirect_uri,HttpServletRequest request) throws Exception {
		return oauth2Service.accessToken(client_id,client_secret,redirect_uri, code, grant_type);
	}
	/**
	 * 获取用户信息
	 * @param access_token   token
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("getUserInfo")
	public Object getUserInfo(String access_token,HttpServletRequest request) throws Exception {
		return oauth2Service.getUserInfo(access_token);
	}
}
