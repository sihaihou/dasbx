package com.reyco.dasbx.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.portal.model.domain.vo.CountryListVO;
import com.reyco.dasbx.portal.service.CountryService;

@RestController
@RequestMapping("country")
public class CountryController {

	@Autowired
	private CountryService countryService;
	
	@GetMapping("list")
	public Object list() {
		List<CountryListVO> countryListVOs = countryService.list();
		return R.success(countryListVOs);
	}
	
}
