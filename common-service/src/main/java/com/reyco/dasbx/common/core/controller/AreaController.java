package com.reyco.dasbx.common.core.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.common.core.model.dto.sys.AreaDeleteDto;
import com.reyco.dasbx.common.core.model.dto.sys.AreaInsertDto;
import com.reyco.dasbx.common.core.model.dto.sys.AreaUpdateDto;
import com.reyco.dasbx.common.core.model.vo.sys.AreaInfoVO;
import com.reyco.dasbx.common.core.service.AreaService;
import com.reyco.dasbx.commons.domain.R;

@RestController
@RequestMapping("/sys/area")
public class AreaController {
	
	@Autowired
	private AreaService areaService;
	
	@PostMapping("initElasticsearchSysArea")
	public Object initElasticsearchSysAccount() throws IOException {
		int count = areaService.initElasticsearchSysArea();
		return R.success(count);
	}
	
	@PostMapping("init")
	public Object init() {
		 areaService.init();
		 return R.success();
	}
	
	@GetMapping("{id}")
	public Object info(@PathVariable("id") Long id) {
		AreaInfoVO areaInfoVO = areaService.get(id);
		return R.success(areaInfoVO);
	}
	@GetMapping("getByParentId")
	public Object getByParentId(Long parentId) {
		List<AreaInfoVO> areaInfoVOs = areaService.getChildsByParentId(parentId);
		if(areaInfoVOs==null || areaInfoVOs.size()==0) {
			AreaInfoVO areaInfoVO = areaService.get(parentId);
			areaInfoVOs.add(areaInfoVO);
		}
		return R.success(areaInfoVOs);
	}
	
	@PostMapping
	public Object save(@RequestBody AreaInsertDto areaInsertDto) throws Exception {
		AreaInfoVO areaInfoVO = areaService.insert(areaInsertDto);
		return R.success(areaInfoVO);
	}
	@PutMapping
	public Object update(@RequestBody AreaUpdateDto areaUpdateDto) {
		areaService.update(areaUpdateDto);
		return R.success("添加成功");
	}
	@DeleteMapping
	public Object delete(@RequestBody AreaDeleteDto areaDeleteDto) {
		areaService.delete(areaDeleteDto);
		return R.success("添加成功");
	}
}
