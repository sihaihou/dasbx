package com.reyco.dasbx.login.core.controller.constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.constant.core.ConstantManager;

@RestController
@RequestMapping("constant")
public class ConstantController {
	@Autowired
	private ConstantManager constantManager;
	
	@GetMapping("get")
	public Object get(String key) {
		System.out.println(key);
		return R.success("key="+key+",value="+constantManager.getProperty(key));
	}
}
