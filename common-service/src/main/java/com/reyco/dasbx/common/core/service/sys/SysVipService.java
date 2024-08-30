package com.reyco.dasbx.common.core.service.sys;

import java.util.List;

import com.reyco.dasbx.model.domain.SysVip;

public interface SysVipService {
	
	SysVip getById(Long id);
	
	List<SysVip> list();
}
