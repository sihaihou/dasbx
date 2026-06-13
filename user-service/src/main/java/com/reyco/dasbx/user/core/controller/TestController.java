package com.reyco.dasbx.user.core.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.rate.limit.annotation.DynamicRateLimit;
import com.reyco.dasbx.rate.limit.annotation.SlidingWindowRateLimit;
import com.reyco.dasbx.user.core.service.TestService;

@RestController
@RequestMapping("test")
public class TestController {
	@Autowired
	private TestService testService;

	@GetMapping
	@SlidingWindowRateLimit(maxRequests=2,unit=TimeUnit.SECONDS)
	public Object test(String test) {
		String result = testService.test(test);
		return R.success(result);
	}
	@DynamicRateLimit(resources="/test/test1")
	@GetMapping("test1")
	public Object test1(int a, String b) {
		Object result = testService.test1(a, b);
		return R.success(result);
	}
}
