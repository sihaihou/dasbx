package com.reyco.dasbx.user.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.id.core.IdGenerator;
import com.reyco.dasbx.jwt.core.JwtUtils;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.user.core.service.TestService;

@Service
public class TestServiceImpl implements TestService{
	@Autowired
	private IdGenerator<Long> idGenerator;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Override
	public String test(String test) {
		System.err.println(test + idGenerator.getGeneratorId());
		return "ok";
	}
	@Override
	public String test1(int a, String b) {
		int i = 1/a;
		SysAccount sysAccount = new SysAccount();
		sysAccount.setId(1L);
		sysAccount.setUsername("admin");
		sysAccount.setPassword("123456");
		String subjectJson = JsonUtils.objToJson(sysAccount);
		String token = jwtUtils.createToken("1", subjectJson);
		String principal = jwtUtils.getSubject(token);
		System.out.println(principal);
		return principal;
	}

}
