package com.reyco.dasbx.user.core.dao.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.user.core.model.domain.SysRole;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleDeletePO;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleInsertPO;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleSelectPO;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleUpdatePO;

/**
 * 
 * @author reyco
 *
 */
public interface SysRoleDao {
	
    /**
     * 根据角色ID查询角色数据信息
     * @param roleId
     * @return
     */
    SysRole get(@Param("roleId") Long roleId);
    
    List<SysRole> getAll();

    /**
     * 查询角色数据信息列表
     * @param SysRoleSelectPO
     * @return
     */
    List<SysRole> list(SysRoleSelectPO sysRoleSelectPO);
    
    /**
     * 用于角色信息数据去重
     * @param map
     * @return
     */
    Integer getCountByNameAndById(Map<String, Object> map);

    /**
     * 创建角色数据信息
     * @param sysRoleInsertPO
     * @return
     */
    int save(SysRoleInsertPO sysRoleInsertPO);

    /**
     * 修改角色数据信息
     * @param sysRoleUpdatePO
     * @return
     */
    int update(SysRoleUpdatePO sysRoleUpdatePO);

    /**
     * 删除角色数据信息(修改deleted值)
     * @param sysRoleDeletePO.roleId
     * @param sysRoleDeletePO.deleted
     * @param sysRoleDeletePO.modifiedBy
     * @return
     */
    int delete(SysRoleDeletePO sysRoleDeletePO);
}