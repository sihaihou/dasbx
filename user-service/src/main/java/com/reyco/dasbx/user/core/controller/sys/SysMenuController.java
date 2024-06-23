package com.reyco.dasbx.user.core.controller.sys;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuDeleteDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuInsertDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuUpdateDto;
import com.reyco.dasbx.user.core.model.vo.sys.SysMenuInsertVO;
import com.reyco.dasbx.user.core.service.sys.SysMenuService;
import com.reyco.dasbx.user.core.service.sys.SysRoleMenuService;

/**
 * 菜单信息管理
 * @author reyco
 *
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController{

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    /**
     * 用户导航菜单
     * @throws AuthenticationException 
     */
    @GetMapping("nav")
    public Object nav() throws AuthenticationException {
    	SysAccountToken token = TokenUtils.getToken();
    	Map<String, List<?>> nav = sysMenuService.nav(token.getId());
        return R.success(nav);
    }
    /**
     * 所有菜单列表(树形结构)
     */
    @GetMapping("tree")
    public Object tree() {
        List<SysMenuDto> menuList = sysMenuService.tree();
        return R.success(menuList);
    }

    /**
     * 菜单信息
     */
    @GetMapping("{menuId}")
    //@RequiresPermissions("sys:menu:info")
    public Object info(@PathVariable("menuId")Long menuId) {
        SysMenuDto sysMenuDto = sysMenuService.get(menuId);
        return R.success(sysMenuDto);
    }

    /**
     * 保存
     * @throws AuthenticationException 
     */
    @PostMapping
    //@RequiresPermissions("sys:menu:save")
    public Object save(@RequestBody SysMenuInsertDto sysMenuInsertDto) throws AuthenticationException {
        // 去重
        Integer count = sysMenuService.getCountByNameAndById(null,sysMenuInsertDto.getParentId(),sysMenuInsertDto.getType(), sysMenuInsertDto.getName());
        if (null == count || count > 0) {
            return R.fail("添加失败，该目录、菜单或按钮已存在...");
        }
        SysMenuInsertVO sysMenuInsertVO = sysMenuService.save(sysMenuInsertDto);
        return R.success(sysMenuInsertVO);
    }

    /**
     * 修改
     * @throws AuthenticationException 
     */
    @PutMapping
    //@RequiresPermissions("sys:menu:update")
    public Object update(@RequestBody SysMenuUpdateDto sysMenuUpdateDto) throws AuthenticationException {
        Integer count = sysMenuService.getCountByNameAndById(sysMenuUpdateDto.getId(),sysMenuUpdateDto.getParentId(),sysMenuUpdateDto.getType(), sysMenuUpdateDto.getName());
        if (null == count || count > 0) {
            return R.fail("修改失败，该目录、菜单或按钮已存在...");
        }
        sysMenuService.update(sysMenuUpdateDto);
        return R.success("success");
    }

    /**
     * 删除
     */
    @DeleteMapping
    //@RequiresPermissions("sys:menu:delete")
    public Object delete(@RequestBody SysMenuDeleteDto sysMenuDeleteDto) {
        if (sysMenuDeleteDto.getId() <= 19) {
            return R.fail("系统菜单，不能删除");
        }
        //判断是否有子菜单或按钮
        List<SysMenuDto> menuList = sysMenuService.queryChildrensByParentId(sysMenuDeleteDto.getId());
        if (menuList.size() > 0) {
            return R.fail("请先删除子菜单或按钮");
        }
        List<String> queryRoleNameList = sysRoleMenuService.queryRoleNamesByMenuId(sysMenuDeleteDto.getId());
        if (queryRoleNameList.size() > 0) {
            return R.fail("此菜单还有关联的角色没有解绑干净！比如："+queryRoleNameList);
        }
        sysMenuService.delete(sysMenuDeleteDto);
        return R.success("success");
    }
    /**
     * 根据父菜单获取子菜单
     * @param parentId
     * @return
     */
    @GetMapping("queryChildrensByParentId")
    //@RequiresPermissions("sys:menu:listByParentId")
    public Object queryChildrensByParentId(Long parentId) {
        List<SysMenuDto> sysMenuDtos = sysMenuService.queryChildrensByParentId(parentId);
        return R.success(sysMenuDtos);
    }
    /**
     * 根据父菜单获取子菜单(不包含按钮)
     * @param parentId
     * @return
     */
    @GetMapping("queryDirectoryByParentId")
    //@RequiresPermissions("sys:menu:listByParentId")
    public Object queryDirectoryByParentId(Long parentId) {
        List<SysMenuDto> sysMenuDtos = sysMenuService.queryDirectoryByParentId(parentId);
        return R.success(sysMenuDtos);
    }
}