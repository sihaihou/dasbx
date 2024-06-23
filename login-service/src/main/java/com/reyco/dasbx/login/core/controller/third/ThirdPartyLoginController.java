package com.reyco.dasbx.login.core.controller.third;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.login.core.service.third.impl.ThirdPartyLoginServiceContext;
import com.reyco.dasbx.model.vo.SysAccountToken;

@Controller
@RequestMapping("login")
public class ThirdPartyLoginController {

	//private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ThirdPartyLoginServiceContext thirdPartyLoginServiceContext;

	@RequestMapping("login")
	public void login(HttpServletResponse response,String type) throws Exception {
		String authorizeUrl = thirdPartyLoginServiceContext.login(type);
		response.sendRedirect(authorizeUrl);
	}
	
	@ResponseBody
	@RequestMapping("callback")
	public Object callback(HttpServletRequest request, HttpServletResponse response, String code, String type)
			throws Exception {
		SysAccountToken accountToken = thirdPartyLoginServiceContext.callback(type,code, request, response);
		return R.success(accountToken);
	}	
	
}
