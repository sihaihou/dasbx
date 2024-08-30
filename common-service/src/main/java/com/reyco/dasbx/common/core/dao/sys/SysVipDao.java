package com.reyco.dasbx.common.core.dao.sys;

import java.util.List;

import com.reyco.dasbx.model.domain.SysVip;

public interface SysVipDao {
	
	SysVip getById(Long id);
	
	List<SysVip> list();
	
}
