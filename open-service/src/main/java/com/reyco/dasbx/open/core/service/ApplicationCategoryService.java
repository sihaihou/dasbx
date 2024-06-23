package com.reyco.dasbx.open.core.service;

import java.util.List;

import com.reyco.dasbx.config.service.BaseService;
import com.reyco.dasbx.model.dto.DeleteDto;
import com.reyco.dasbx.model.dto.InsertDto;
import com.reyco.dasbx.model.dto.ListDto;
import com.reyco.dasbx.model.dto.UpdateDto;
import com.reyco.dasbx.model.vo.ListVO;
import com.reyco.dasbx.open.core.model.vo.applicationCategory.ApplicationCategoryInfoVo;

public interface ApplicationCategoryService extends BaseService<ApplicationCategoryInfoVo, ListVO, ListDto, UpdateDto, InsertDto, DeleteDto> {
	
	List<ApplicationCategoryInfoVo> getChildsByParentId(Long id);
	
}
