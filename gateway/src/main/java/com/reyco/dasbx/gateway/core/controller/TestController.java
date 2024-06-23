package com.reyco.dasbx.gateway.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
	
	@GetMapping("test")
	public Object test(String s) {
		System.out.println(s);
		return "OK";
	}
}
