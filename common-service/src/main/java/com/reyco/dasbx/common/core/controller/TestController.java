package com.reyco.dasbx.common.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.id.core.IdGenerator;

@RestController
@RequestMapping("test")
public class TestController {
	@Autowired
	private IdGenerator<Long> idGenerator;
	
	@GetMapping
	public Object test(String test){
		System.err.println(test+idGenerator.getGeneratorId());
		return "ok";
	}
	@PostMapping("test1")
	public Object test1(String test){
		System.err.println(test);
		return R.success();
	}
}
