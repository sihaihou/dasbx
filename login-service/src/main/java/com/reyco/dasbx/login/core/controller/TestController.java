package com.reyco.dasbx.login.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;

@RestController
@RequestMapping("test")
public class TestController {
	
	@GetMapping("test")
	public Object code(String s) {
		System.out.println(s);
		return R.success("ok");
	}
}
