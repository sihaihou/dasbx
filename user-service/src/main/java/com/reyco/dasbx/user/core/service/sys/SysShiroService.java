package com.reyco.dasbx.user.core.service.sys;

import java.util.Set;

import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.vo.SysAccountToken;

/**
 * 权限管理
 * @author reyco
 *
 */
public interface SysShiroService {

    /**
     * 获取用户权限列表
     * @param userId
     * @return
     */
    Set<String> getUserPermissions(Long userId);
    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    SysAccount getUser(Long userId);

    /**
     * 获取用户对应的数据库存贮的token值
     * @param token
     * @return
     */
    SysAccountToken getToken(String token);
}