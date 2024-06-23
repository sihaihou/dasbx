package com.reyco.dasbx.open.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperInsertDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperPageDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperReviewDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperUpdateDto;
import com.reyco.dasbx.open.core.model.vo.developer.DeveloperInfoVO;
import com.reyco.dasbx.open.core.service.developer.DeveloperService;

@RestController
@RequestMapping("developer")
public class DeveloperController {
	@Autowired
	private DeveloperService developerService;
	
	@GetMapping("{id}")
	public Object get(@PathVariable("id") Long id) {
		DeveloperInfoVO developerInfoVO = developerService.get(id);
		return R.success(developerInfoVO);
	}
	
	@GetMapping("search")
	public Object search(DeveloperPageDto developerPageDto) throws AuthenticationException {
		PageInfo<DeveloperInfoVO> search = developerService.searchPage(developerPageDto);
		return R.success(search);
	}
	
	@GetMapping("currentDeveloper")
	public Object currentDeveloper() throws Exception {
		DeveloperInfoVO developerInfoVO = developerService.currentDeveloper();
		return R.success(developerInfoVO);
	}
	@PostMapping
	public Object create(@RequestBody DeveloperInsertDto developerInsertDto) throws Exception {
		developerService.insert(developerInsertDto);
		return R.success();
	}
	@PutMapping
	public Object update(@RequestBody DeveloperUpdateDto developerUpdateDto) throws Exception {
		developerService.update(developerUpdateDto);
		return R.success();
	}
	@PatchMapping
	public Object update(@RequestBody DeveloperReviewDto developerReviewDto) throws Exception {
		developerService.review(developerReviewDto);
		return R.success();
	}
}
