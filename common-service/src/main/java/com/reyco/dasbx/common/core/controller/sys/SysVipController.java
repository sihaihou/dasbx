package com.reyco.dasbx.common.core.controller.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.common.core.service.sys.SysVipService;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.SysVip;

@RestController
@RequestMapping("vip")
public class SysVipController {

	@Autowired
	private SysVipService sysVipService;
	
	@GetMapping("/{id}")
	public Object info(@PathVariable("id")Long id) {
		SysVip sysVip = sysVipService.getById(id);
		return R.success(sysVip);
	}
	
	@GetMapping("/list")
	public Object list() {
		List<SysVip> sysVips = sysVipService.list();
		return R.success(sysVips);
	}
	
	
}
