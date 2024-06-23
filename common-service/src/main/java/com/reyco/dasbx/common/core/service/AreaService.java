package com.reyco.dasbx.common.core.service;

import java.io.IOException;
import java.util.List;

import com.reyco.dasbx.common.core.model.dto.sys.AreaDeleteDto;
import com.reyco.dasbx.common.core.model.dto.sys.AreaInsertDto;
import com.reyco.dasbx.common.core.model.dto.sys.AreaListDto;
import com.reyco.dasbx.common.core.model.dto.sys.AreaUpdateDto;
import com.reyco.dasbx.common.core.model.vo.sys.AreaInfoVO;
import com.reyco.dasbx.common.core.model.vo.sys.AreaListVO;
import com.reyco.dasbx.config.service.BaseService;

public interface AreaService extends BaseService<AreaInfoVO, AreaListVO, AreaListDto, AreaUpdateDto, AreaInsertDto, AreaDeleteDto> {
	
	AreaInfoVO getByName(String name);
	
	List<AreaInfoVO> getChildsByParentId(Long id);
	
	List<AreaInfoVO> getAll();
	
	/**
	 * 初始化区域
	 */
	void init();
	
	Integer initElasticsearchSysArea() throws IOException;
}
