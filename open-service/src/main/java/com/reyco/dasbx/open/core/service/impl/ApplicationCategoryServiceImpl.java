package com.reyco.dasbx.open.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.model.dto.DeleteDto;
import com.reyco.dasbx.model.dto.InsertDto;
import com.reyco.dasbx.model.dto.ListDto;
import com.reyco.dasbx.model.dto.UpdateDto;
import com.reyco.dasbx.model.vo.ListVO;
import com.reyco.dasbx.open.core.dao.ApplicationCategoryDao;
import com.reyco.dasbx.open.core.model.domain.ApplicationCategory;
import com.reyco.dasbx.open.core.model.vo.applicationCategory.ApplicationCategoryInfoVo;
import com.reyco.dasbx.open.core.service.ApplicationCategoryService;

@Service
public class ApplicationCategoryServiceImpl implements ApplicationCategoryService{
	
	@Autowired
	private ApplicationCategoryDao applicationCategoryDao;
	
	@Override
	public List<ApplicationCategoryInfoVo> getChildsByParentId(Long id) {
		List<ApplicationCategory> applicationCategorys = applicationCategoryDao.getChildsByParentId(id);
		List<ApplicationCategoryInfoVo> applicationCategoryInfoVos = Convert.sourceListToTargetList(applicationCategorys, ApplicationCategoryInfoVo.class);
		return applicationCategoryInfoVos;
	}

	@Override
	public ApplicationCategoryInfoVo get(Long id) {
		ApplicationCategory applicationCategory = applicationCategoryDao.getById(id);
		ApplicationCategoryInfoVo applicationCategoryInfoVo = Convert.sourceToTarget(applicationCategory, ApplicationCategoryInfoVo.class);
		return applicationCategoryInfoVo;
	}

	@Override
	public List<ListVO> list(ListDto t1) {
		return null;
	}

	@Override
	public void update(UpdateDto t2) {
		
	}

	@Override
	public ApplicationCategoryInfoVo insert(InsertDto t3) throws Exception {
		return null;
	}

	@Override
	public void delete(DeleteDto t4) {
		
	}

}
