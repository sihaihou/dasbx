package com.reyco.dasbx.open.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.open.core.model.vo.applicationCategory.ApplicationCategoryInfoVo;
import com.reyco.dasbx.open.core.service.ApplicationCategoryService;

@RestController
@RequestMapping("applicationCategory")
public class ApplicationCategoryController {
	
	@Autowired
	private ApplicationCategoryService applicationCategoryService;
	
	@GetMapping("listByParentId")
	public Object listByParentId(Long parentId) {
		List<ApplicationCategoryInfoVo> applicationCategoryInfoVos = applicationCategoryService.getChildsByParentId(parentId);
		ApplicationCategoryInfoVo applicationCategoryInfoVoDefault = new ApplicationCategoryInfoVo();
		applicationCategoryInfoVoDefault.setId(null);
		applicationCategoryInfoVoDefault.setName("请选择");
		applicationCategoryInfoVos.add(0, applicationCategoryInfoVoDefault);
		if(applicationCategoryInfoVos.size()==1) {
			ApplicationCategoryInfoVo applicationCategoryInfoVo = applicationCategoryService.get(parentId);
			applicationCategoryInfoVos.add(applicationCategoryInfoVo);
		}
		return R.success(applicationCategoryInfoVos);
	}

}
