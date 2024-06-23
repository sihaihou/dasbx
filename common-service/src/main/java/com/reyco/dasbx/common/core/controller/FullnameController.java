package com.reyco.dasbx.common.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.common.core.service.FullnameService;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.Fullname;

@RestController
@RequestMapping("fullname")
public class FullnameController {
	
	@Autowired
	private FullnameService fullnameService;
	
	@GetMapping("randomFullName")
	public Object randomFullName() {
		Fullname fullname = fullnameService.randomFullname();
		return R.success(fullname);
	}
	
}
