package com.reyco.dasbx.user.core.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.user.core.dao.sys.SysUserRoleDao;
import com.reyco.dasbx.user.core.model.po.sys.SysUserRoleDeletePO;
import com.reyco.dasbx.user.core.model.po.sys.SysUserRoleInsertPO;
import com.reyco.dasbx.user.core.service.sys.SysUserRoleService;

/**
 * 用户与角色关联信息
 * @author reyco
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public List<Long> queryRoleIdsByUserId(Long userId) {
		return sysUserRoleDao.queryRoleIdsByUserId(userId);
	}
	@Override
	public void saveOrUpdate(Long userId, List<Long> roleIdList) {
		// 先删除用户与角色关系
		List<Long> queryRoleIdList = sysUserRoleDao.queryRoleIdsByUserId(userId);
		if (queryRoleIdList.size() > 0) {
			SysUserRoleDeletePO sysRoleMenuDeletePO = new SysUserRoleDeletePO();
			sysRoleMenuDeletePO.setUserId(userId);
			sysUserRoleDao.delete(sysRoleMenuDeletePO);
		}
		if (roleIdList==null || roleIdList.size() == 0) {
			return;
		}
		// 保存用户与角色关系
		SysUserRoleInsertPO sysUserRoleInsertPO = new SysUserRoleInsertPO();
		sysUserRoleInsertPO.setUserId(userId);
		sysUserRoleInsertPO.setRoleIdList(roleIdList);
		sysUserRoleDao.save(sysUserRoleInsertPO);
	}
	
	@Override
	public List<String> queryUseruamesByRoleId(Long roleId) {
		return sysUserRoleDao.queryUseruamesByRoleId(roleId);
	}

}