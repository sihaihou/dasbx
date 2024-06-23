package com.reyco.dasbx.common.core.dao.sys;

import java.util.List;

import com.reyco.dasbx.common.core.model.po.sys.SysLogInsertPO;
import com.reyco.dasbx.common.core.model.po.sys.SysLogPO;
import com.reyco.dasbx.common.core.model.po.sys.SysLogUpdatePO;
import com.reyco.dasbx.model.domain.SysLog;

public interface SysLogDao{

	SysLog getById(Long id);
	
	List<SysLog> listByCode(Long code);
	
	List<SysLog> list(SysLogPO sysLogPO);
	
	void save(SysLogInsertPO sysLogInsertPO);
	
	void updateByCode(SysLogUpdatePO sysLogUpdatePO);
}
