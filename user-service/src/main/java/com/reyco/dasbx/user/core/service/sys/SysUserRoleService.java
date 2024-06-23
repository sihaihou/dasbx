package com.reyco.dasbx.user.core.service.sys;

import java.util.List;

/**
 * 用户与角色关联信息
 * @author reyco
 *
 */
public interface SysUserRoleService {
	 /**
     * 根据用户ID，获取角色ID列表
     * @param userId
     * @return
     */
    List<Long> queryRoleIdsByUserId(Long userId);
    /**
     * 根据roleID获取用户名称列表
     * @param roleId
     * @return
     */
    List<String> queryUseruamesByRoleId(Long roleId);
    /**
     * 保存或者修改用户与角色关系
     * @param userId
     * @param roleList
     */
    void saveOrUpdate(Long userId, List<Long> roleList);
   
}