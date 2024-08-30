package com.reyco.dasbx.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.portal.model.domain.vo.TypeListVO;
import com.reyco.dasbx.portal.service.TypeService;

@RestController
@RequestMapping("type")
public class TypeController {

	@Autowired
	private TypeService typeService;
	
	@GetMapping("list")
	public Object list() {
		List<TypeListVO> typeListVOs = typeService.list();
		return R.success(typeListVOs);
	}
	
}
