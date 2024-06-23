package com.reyco.dasbx.user.core.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.user.core.model.po.sys.SysUserRoleDeletePO;
import com.reyco.dasbx.user.core.model.po.sys.SysUserRoleInsertPO;

/**
 * 用户角色关联信息
 * @author reyco
 *
 */
public interface SysUserRoleDao {

    /**
     * 删除用户与角色绑定关系数据信息
     * @param sysUserRoleDeletePO.userId
     * @return
     */
    int delete(SysUserRoleDeletePO sysUserRoleDeletePO);
    /**
     * 绑定用户与角色关系
     * @param sysUserRoleInsertPO.userId
     * @param sysUserRoleInsertPO.roleIdList
     * @return
     */
    int save(SysUserRoleInsertPO sysUserRoleInsertPO);

    /**
     * 根据用户ID获取角色ID
     * @param userId
     * @return
     */
    List<Long> queryRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据roleID获取用户名称列表
     * @param roleId
     * @param deleted
     * @return
     */
    List<String> queryUseruamesByRoleId(@Param("roleId")Long roleId);
}