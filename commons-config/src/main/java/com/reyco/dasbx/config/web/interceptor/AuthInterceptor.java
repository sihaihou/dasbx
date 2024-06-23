package com.reyco.dasbx.config.web.interceptor;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.model.vo.SysAccountToken;

/**
 * token认证
 * @author reyco
 *
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
	public final static String INTERCEPTOR_NAME = "auth";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		if(!validateToken(request, response)) {
			return false;
		}
		return true;
	}
	private Boolean validateToken(HttpServletRequest request, HttpServletResponse response) throws IOException, AuthenticationException {
		String token = TokenUtils.getTokenString(request);
		String deviceId = TokenUtils.getDeviceId(request);
		String deviceType = TokenUtils.getDeviceType(request);
		if (StringUtils.isBlank(token) || StringUtils.isBlank(deviceId) || StringUtils.isBlank(deviceType)) {
			R<?> r = R.fail("没有登录", "no login!");
			OutputStream os = response.getOutputStream();
			String json = JSONObject.toJSONString(r);
			os.write(json.getBytes("UTF-8"));
			os.flush();
			return false;
		}
		SysAccountToken accountToken = TokenUtils.getToken(deviceId,deviceType,token);
		if (accountToken==null) {
			R<?> r = R.fail("登录过期", "Login Invalid");
			OutputStream os = response.getOutputStream();
			String json = JSONObject.toJSONString(r);
			os.write(json.getBytes("UTF-8"));
			os.flush();
			return false;
		}
		return true;
	}
}
