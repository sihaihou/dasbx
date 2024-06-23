package com.reyco.dasbx.user.core.service.sys.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.user.core.dao.sys.SysRoleMenuDao;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuDto;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleMenuDeletePO;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleMenuInsertPO;
import com.reyco.dasbx.user.core.service.sys.SysRoleMenuService;

/**
 * 角色和菜单关联
 * @author reyco
 *
 */
@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    
    @Override
    public List<Long> queryMenuIdList(Long roleId) {
    	List<SysMenuDto> roleMenus = sysRoleMenuDao.queryMenusByRoleId(roleId);
    	Set<Long> menuParentIds = new HashSet<>();
    	roleMenus.stream().forEach(sysMenuDto->{
    		menuParentIds.add(sysMenuDto.getParentId());
    	});
    	return roleMenus.stream()
    			.filter(menu->!menuParentIds.contains(menu.getId()))
    			.map(menu->menu.getId())
    			.collect(Collectors.toList());
    }

    @Override
    public void saveOrUpdate(Long roleId, List<Long> menuIdList){
    	//删除角色与菜单关系
        List<Long> queryMenuIdList = sysRoleMenuDao.queryMenuIdsByRoleId(roleId);
        if (queryMenuIdList.size() > 0) {
        	SysRoleMenuDeletePO sysRoleMenuDeletePO = new SysRoleMenuDeletePO();
        	sysRoleMenuDeletePO.setRoleId(roleId);
        	sysRoleMenuDao.delete(sysRoleMenuDeletePO);
        }
        if (menuIdList==null || menuIdList.size() == 0) {
            return;
        }
        //保存菜单与角色的关系
        SysRoleMenuInsertPO sysRoleMenuInsertPO = new SysRoleMenuInsertPO();
        sysRoleMenuInsertPO.setRoleId(roleId);
        sysRoleMenuInsertPO.setMenuIdList(menuIdList);
        sysRoleMenuDao.save(sysRoleMenuInsertPO);
    }
    @Override
    public List<String> queryRoleNamesByMenuId(Long menuId) {
    	return sysRoleMenuDao.queryRoleNamesByMenuId(menuId);
    }
}