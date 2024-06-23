package com.reyco.dasbx.common.core.service.sys;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.model.domain.SysEsError;

public interface SysEsErrorService {
	
	SysEsError get(Long id);
	
	PageInfo<SysEsError> search(SysEsError SysEsError);
	
}
