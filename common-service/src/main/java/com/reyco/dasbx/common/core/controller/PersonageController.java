package com.reyco.dasbx.common.core.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.common.core.model.dto.personage.PersonageInsertDto;
import com.reyco.dasbx.common.core.model.dto.personage.PersonageSearchDto;
import com.reyco.dasbx.common.core.model.vo.personage.PersonageInfoVO;
import com.reyco.dasbx.common.core.service.PersonageService;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.es.core.search.ElasticsearchVO;

@RestController
@RequestMapping("personage")
public class PersonageController {
	@Autowired
	private PersonageService personageService;
	@PostMapping("init")
	public Object init() throws IOException {
		int init = personageService.initPersonage();
		return R.success(init);
	}
	@PostMapping("initElasticsearchPersonage")
	public Object initElasticsearchPersonage() throws IOException {
		personageService.initElasticsearchPersonage();
		return R.success();
	}
	@PostMapping("save")
	public Object save(@RequestBody PersonageInsertDto personageInsertDto) throws Exception {
		personageService.insert(personageInsertDto);
		return R.success();
	}
	@PostMapping("randomPersonage")
	public Object randomPersonage() throws IOException {
		int init = personageService.randomPersonage();
		return R.success(init);
	}
	
	@GetMapping("suggestion")
	public Object getSuggestion(String keyword) throws Exception {
		List<String> suggestions = personageService.getSuggestion(keyword);
		return R.success(suggestions);
	}
	@GetMapping("search")
	public Object search(PersonageSearchDto personageSearchDto) throws Exception {
		ElasticsearchVO<PersonageInfoVO> search = personageService.search(personageSearchDto);
		return R.success(search);
	}
}
