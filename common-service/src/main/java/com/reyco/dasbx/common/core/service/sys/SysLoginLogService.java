package com.reyco.dasbx.common.core.service.sys;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogInsertDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogLoginUpdateDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogLogoutUpdateDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogPageDto;
import com.reyco.dasbx.common.core.model.vo.sys.SysLoginLogInfoVO;
import com.reyco.dasbx.common.core.model.vo.sys.SysLoginLogListVO;

public interface SysLoginLogService {
	
	SysLoginLogInfoVO get(Long id);
	
	List<SysLoginLogListVO> listByUserId(Long userId);
	
	PageInfo<SysLoginLogListVO> search(SysLoginLogPageDto sysLoginLogPageDto);
	
	SysLoginLogInfoVO insert(SysLoginLogInsertDto sysLoginLogInsertDto);
	
	void updateLogin(SysLoginLogLoginUpdateDto sysLoginLogLoginUpdateDto);
	
	void updateLogout(SysLoginLogLogoutUpdateDto sysLoginLogLogoutUpdateDto);
}
