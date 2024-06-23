package com.reyco.dasbx.common.core.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.common.core.model.domain.sys.SysMessage;
import com.reyco.dasbx.common.core.model.po.sys.message.SysMessageDeletePO;
import com.reyco.dasbx.common.core.model.po.sys.message.SysMessageInsertPO;
import com.reyco.dasbx.common.core.model.po.sys.message.SysMessageSelectPO;
import com.reyco.dasbx.common.core.model.po.sys.message.SysMessageUpdateHandlePO;
import com.reyco.dasbx.common.core.model.po.sys.message.SysMessageUpdateReadPO;

public interface SysMessageDao {

	SysMessage getById(Long id);
	
	List<SysMessage> list(SysMessageSelectPO sysMessageSelectPO );
	
	void insert(SysMessageInsertPO sysMessageInsertPO);
	
	void updateRead(SysMessageUpdateReadPO sysMessageUpdateReadPO);
	
	void updateHandle(SysMessageUpdateHandlePO sysMessageUpdateHandlePO);
	
	void deleteById(SysMessageDeletePO sysMessageDeletePO);
	
	Integer getCountByUserId(@Param("userId") Long userId);
}
