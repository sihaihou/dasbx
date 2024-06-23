package com.reyco.dasbx.user.core.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.model.dao.BaseDao;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.po.DeletePO;
import com.reyco.dasbx.model.po.InsertPO;
import com.reyco.dasbx.model.po.UpdatePO;
import com.reyco.dasbx.user.core.model.po.AccountBindDeveloperPO;
import com.reyco.dasbx.user.core.model.po.sys.SysAccountDisableOrEnablePO;
import com.reyco.dasbx.user.core.model.po.sys.SysAccountSelectPO;

public interface SysAccountDao extends BaseDao<SysAccount, InsertPO, DeletePO, UpdatePO, SysAccountSelectPO>{
	
	SysAccount getByEmail(@Param("email") String email);
	
	SysAccount getByUsername(@Param("username")String username);
	/**
	 * 查询用是否存在用以户去重
	 * @param username
	 * @param userId
	 * @return
	 */
    int getCountByNameAndById(@Param("username") String username, @Param("id") Long userId);
	/**
	 * 禁用启用
	 * @param sysAccountDisableOrEnablePO
	 */
	void updateState(SysAccountDisableOrEnablePO sysAccountDisableOrEnablePO);
	
	void bindDeveloper(AccountBindDeveloperPO accountBindDeveloperPO);
	
	Long getMaxId();
	
	List<SysAccount> getListByLimit(@Param("startId")Long startId,@Param("endId")Long endId);
}
