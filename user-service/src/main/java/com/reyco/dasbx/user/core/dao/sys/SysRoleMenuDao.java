package com.reyco.dasbx.user.core.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.user.core.model.dto.sys.SysMenuDto;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleMenuDeletePO;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleMenuInsertPO;

/**
 * 菜单角色关联信息
 * @author reyco
 *
 */
public interface SysRoleMenuDao {

    /**
     * 删除角色和菜单的关联关系
     * @param sysRoleMenuDeletePO.roleId
     * @return
     */
    int delete(SysRoleMenuDeletePO sysRoleMenuDeletePO);

    /**
     * 绑定角色和菜单关系数据信息
     * @param sysRoleMenuInsertPO
     * @return
     */
    int save(SysRoleMenuInsertPO sysRoleMenuInsertPO);

    /**
     * 根据角色ID，获取菜单ID列表
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);
    /**
     * 根据角色ID，获取菜单列表
     * @param roleId
     * @return
     */
    List<SysMenuDto> queryMenusByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据menuId获取角色名称列表
     * @param menuId
     * @return
     */
    List<String> queryRoleNamesByMenuId(@Param("menuId")Long menuId);
    
}