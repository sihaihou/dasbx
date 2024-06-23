package com.reyco.dasbx.user.core.service.sys;

import java.util.List;

/**
 * 角色和菜单关联
 * @author reyco
 *
 */
public interface SysRoleMenuService {

    /**
     * 根据角色ID，获取菜单ID列表(只返回叶子节点数据)
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdList(Long roleId);
    
    /**
     * 根据menuId获取角色名称列表
     * @param menuId
     * @return
     */
    List<String> queryRoleNamesByMenuId(Long menuId);
    
    /**
     * 角色与菜单保存或者修改
     * @param roleId
     * @param menuIdList
     */
    void saveOrUpdate(Long roleId, List<Long> menuIdList);
}