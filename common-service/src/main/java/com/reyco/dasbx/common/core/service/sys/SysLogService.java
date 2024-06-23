package com.reyco.dasbx.common.core.service.sys;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.common.core.model.dto.sys.SysLogInsertDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLogPageDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLogUpdateDto;
import com.reyco.dasbx.common.core.model.vo.sys.SysLogInfoVO;
import com.reyco.dasbx.common.core.model.vo.sys.SysLogListVO;

public interface SysLogService {
	
	SysLogInfoVO get(Long id);
	
	List<SysLogListVO> listByCode(Long code);
	
	PageInfo<SysLogListVO> search(SysLogPageDto sysLogPageDto);
	
	SysLogInfoVO insert(SysLogInsertDto sysLogInsertDto);
	
	void updateByCode(SysLogUpdateDto sysLogUpdateDto);
	
}
