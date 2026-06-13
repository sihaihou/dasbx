package com.reyco.dasbx.user.core.service;

import com.reyco.dasbx.commons.exception.BusinessException;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;

public interface SysMessageService  {
	
	void insert(SysMessageInsertDto sysMessageInsertDto) throws BusinessException ;
	
}
