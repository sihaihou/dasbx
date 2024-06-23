package com.reyco.dasbx.common.core.dao.sys;

import java.util.List;

import com.reyco.dasbx.common.core.model.domain.sys.SysLoginLog;
import com.reyco.dasbx.common.core.model.po.sys.SysLoginLogInsertPO;
import com.reyco.dasbx.common.core.model.po.sys.SysLoginLogLoginUpdatePO;
import com.reyco.dasbx.common.core.model.po.sys.SysLoginLogLogoutUpdatePO;
import com.reyco.dasbx.common.core.model.po.sys.SysLoginLogPO;

public interface SysLoginLogDao{

	SysLoginLog getById(Long id);
	
	SysLoginLog getByCode(Long code);
	
	List<SysLoginLog> listByUserId(Long userId);
	
	List<SysLoginLog> list(SysLoginLogPO sysLoginLogPO);
	
	/**
	 * 登录
	 * @param sysLoginLogInsertPO
	 */
	void save(SysLoginLogInsertPO sysLoginLogInsertPO);
	
	/**
	 * 更新登录
	 * @param sysLoginLogLoginUpdatePO
	 */
	void updateLogin(SysLoginLogLoginUpdatePO sysLoginLogLoginUpdatePO);
	/**
	 * 更新登出
	 * @param sysLoginLogUpdatePO
	 */
	void updateLogout(SysLoginLogLogoutUpdatePO sysLoginLogUpdatePO);
}
