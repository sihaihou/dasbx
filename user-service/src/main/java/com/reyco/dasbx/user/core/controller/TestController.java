package com.reyco.dasbx.user.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.id.core.IdGenerator;
import com.reyco.dasbx.jwt.core.JwtUtils;
import com.reyco.dasbx.model.domain.SysAccount;

@RestController
@RequestMapping("test")
public class TestController {
	
	@Autowired
	private IdGenerator<Long> idGenerator;
	
	@Autowired
	private JwtUtils jwtUtils;

	@GetMapping
	public Object test(String test) {
		System.err.println(test + idGenerator.getGeneratorId());
		return "ok";
	}

	@GetMapping("test1")
	public Object test1(int a) {
		int i = 1/a;
		SysAccount sysAccount = new SysAccount();
		sysAccount.setId(1L);
		sysAccount.setUsername("admin");
		sysAccount.setPassword("123456");
		String subjectJson = JsonUtils.objToJson(sysAccount);
		String token = jwtUtils.createToken("1", subjectJson);
		String principal = jwtUtils.getSubject(token);
		System.out.println(principal);
		return R.success(principal);
	}
}
