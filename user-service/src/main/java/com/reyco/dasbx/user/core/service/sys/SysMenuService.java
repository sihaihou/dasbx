package com.reyco.dasbx.user.core.service.sys;

import java.util.List;
import java.util.Map;

import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuDeleteDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuInsertDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuUpdateDto;
import com.reyco.dasbx.user.core.model.vo.sys.SysMenuInsertVO;

/**
 * 菜单信息
 * @author reyco
 *
 */
public interface SysMenuService {

    /**
     * 获取数据
     * @param menuId
     * @return
     */
    SysMenuDto get(Long menuId);
    /**
     * 根据类型、parentId、名称或者非id查询count
     * @param menuId  		required=false
     * @param menuParentId	required=true
     * @param menuType		required=true
     * @param menuName		required=true
     * @return
     */
    Integer getCountByNameAndById(Long menuId,Long menuParentId,Integer menuType,String menuName);
    /**
     * 保存菜单
     * @param SysMenuInsertDto
     * @throws AuthenticationException
     */
    SysMenuInsertVO save(SysMenuInsertDto SysMenuInsertDto);
    /**
     * 修改
     * @param sysMenuUpdateDto
     * @throws AuthenticationException
     */
    void update(SysMenuUpdateDto sysMenuUpdateDto);
    /**
     *  删除菜单
     * @param sysMenuDeleteDto
     */
    void delete(SysMenuDeleteDto sysMenuDeleteDto);
    
    /**
     * nav
     */
    Map<String, List<?>> nav(Long userId);
    
    /**
     * 查询菜单列表
     */
    List<SysMenuDto> tree();
    
    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<SysMenuDto> queryChildrensByParentId(Long parentId);

    /**
     * 根据父目录获取子目录
     * @param parentId
     * @return
     */
    List<SysMenuDto> queryDirectoryByParentId(Long parentId);
   
}