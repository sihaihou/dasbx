package com.reyco.dasbx.common.core.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.common.core.model.po.sys.SysEsErroListPO;
import com.reyco.dasbx.common.core.model.po.sys.SysEsErrorInsertPO;
import com.reyco.dasbx.common.core.model.po.sys.SysEsErrorProcessPO;
import com.reyco.dasbx.model.domain.SysEsError;

public interface SysEsErrorDao {
	
	SysEsError getById(Long id);
	
	List<SysEsError> listByIndex(@Param("index")String index);
	
	List<SysEsError> list(SysEsErroListPO sysEsErroListPO);
	
	void batchInsert(SysEsErrorInsertPO sysEsErrorInsertPOs);
	
	void processe(SysEsErrorProcessPO sysEsErrorProcessPO);
}
