package com.reyco.dasbx.portal.service;

import com.reyco.dasbx.config.exception.core.BusinessException;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;

public interface SysMessageService  {
	
	void insert(SysMessageInsertDto sysMessageInsertDto) throws BusinessException ;
	
}
