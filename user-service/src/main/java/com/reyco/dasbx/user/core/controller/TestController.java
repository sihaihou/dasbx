package com.reyco.dasbx.user.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.resource.annotation.Resource;
import com.reyco.dasbx.user.core.service.TestService;

@RestController
@RequestMapping("test")
public class TestController {
	@Autowired
	private TestService testService;

	@GetMapping
	public Object test(String test) {
		String result = testService.test(test);
		return R.success(result);
	}
	@Resource
	@GetMapping("test1")
	public Object test1(int a, String b) {
		Object result = testService.test1(a, b);
		return R.success(result);
	}
}
