package com.reyco.dasbx.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.portal.model.domain.vo.YearListVO;
import com.reyco.dasbx.portal.service.YearService;

@RestController
@RequestMapping("year")
public class YearController {

	@Autowired
	private YearService yearService;
	
	@GetMapping("list")
	public Object list() {
		List<YearListVO> yearListVO = yearService.list();
		return R.success(yearListVO);
	}
	
}
