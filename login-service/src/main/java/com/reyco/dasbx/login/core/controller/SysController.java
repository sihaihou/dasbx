package com.reyco.dasbx.login.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.login.core.service.SysService;

@RestController
@RequestMapping("sys")
public class SysController {
	
	@Autowired
	private SysService sysService;
	
	@GetMapping("code")
	public Object code(Long code) {
		 Long sysCode = sysService.getCode(code);
		 return R.success(sysCode);
	}
}
