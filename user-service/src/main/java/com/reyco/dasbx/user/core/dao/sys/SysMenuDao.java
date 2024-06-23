package com.reyco.dasbx.user.core.dao.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.user.core.model.domain.SysMenu;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuDto;
import com.reyco.dasbx.user.core.model.po.sys.SysMenuDeletePO;
import com.reyco.dasbx.user.core.model.po.sys.SysMenuInsertPO;
import com.reyco.dasbx.user.core.model.po.sys.SysMenuUpdatePO;

/**
 * 
 * @author reyco
 *
 */
public interface SysMenuDao {

	/**
	 * 根据菜单ID获取菜单数据信息
	 * 
	 * @param menuId
	 * @return
	 */
	SysMenu get(Long menuId);

	/**
	 * 根据菜单type和parentId和name或者非id查询count
	 * @param map
	 * @return
	 */
	Integer getCountByNameAndById(Map<String, Object> map);
	
	/**
	 * 添加菜单数据信息
	 * 
	 * @param sysMenuInsertPO
	 * @return
	 */
	int save(SysMenuInsertPO sysMenuInsertPO);

	/**
	 * 修改菜单数据信息
	 * 
	 * @param sysMenuUpdatePO
	 */
	void update(SysMenuUpdatePO sysMenuUpdatePO);

	/**
	 * 根据菜单ID删除菜单数据信息(修改deleted值)
	 * @param sysMenuDeletePO
	 */
	void delete(SysMenuDeletePO sysMenuDeletePO);

	/**
	 * 获取admin的所有菜单
	 * @return
	 */
	List<SysMenuDto> queryAll();
	/**
	 * 获取用户的所有菜单
	 * @return
	 */
	List<SysMenuDto> queryAllByUserId(@Param("userId") Long userId);
	
	/**
	 * 查询admin的所有权限 (nav的权限)
	 * @return
	 */
	List<String> queryPerms();
	/**
	 *  查询用户的所有权限 (nav的权限)
	 * @param userId
	 * @return
	 */
	List<String> queryPermsByUserId(@Param("userId") Long userId);
	
	
	/**
	 * 获取用户的所有菜单（不包含按钮）（tree）
	 * @return
	 */
	List<SysMenuDto> queryMenus();
	/**
	 * 获取用户的所有菜单（不包含按钮）（tree）
	 * @param userId
	 * @return
	 */
	List<SysMenuDto> queryMenusByUserId(@Param("userId") Long userId);
	
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId
	 * @return
	 */
	List<SysMenuDto> queryChildrensByParentId(@Param("parentId") Long parentId);
	/**
	 * 根据父目录获取子目录(不包含按钮)
	 * @param parentId
	 * @return
	 */
	List<SysMenuDto> queryDirectoryByParentId(@Param("parentId") Long parentId);
	
}