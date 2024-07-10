package com.reyco.dasbx.user.core.controller.sys;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import com.reyco.dasbx.es.core.search.SearchVO;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleDeleteDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleInsertDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleSearchDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleUpdateDto;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleReq;
import com.reyco.dasbx.user.core.model.vo.sys.SysRoleInfoVO;
import com.reyco.dasbx.user.core.service.sys.SysRoleService;
import com.reyco.dasbx.user.core.service.sys.SysUserRoleService;

/**
 * 角色管理
 * 
 * @author reyco
 *
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@PostMapping("initElasticsearchSysRole")
	public Object initElasticsearchSysAccount() throws IOException {
		int count = sysRoleService.initElasticsearchSysRole();
		return R.success(count);
	}
	/**
	 * 获取角色详细信息
	 * 
	 * @param roleId
	 * @return
	 */
	@GetMapping("{roleId}")
	// @RequiresPermissions("sys:role:info")
	public Object getRole(@PathVariable("roleId") Long roleId) {
		SysRoleInfoVO sysRoleInfoVO = sysRoleService.get(roleId);
		return R.success(sysRoleInfoVO);
	}
	@GetMapping("suggestion")
	public Object getSuggestion(String keyword) throws Exception {
		List<String> suggestions = sysRoleService.getSuggestion(keyword);
		return R.success(suggestions);
	}
	/**
	 * 获取角色列表信息
	 * 
	 * @param sysRolePageReq
	 * @return
	 * @throws IOException 
	 */
	@GetMapping("search")
	// @RequiresPermissions("sys:role:list")
	public Object list(SysRoleSearchDto sysRoleSearchDto) throws IOException {
		SearchVO<SysRoleInfoVO> search = sysRoleService.search(sysRoleSearchDto);
		return R.success(search);
	}

	/**
	 * 查询角色列表信息
	 * 
	 * @param sysRoleReq
	 * @return
	 */
	@GetMapping("list")
	// @RequiresPermissions("sys:role:select")
	public Object selectRole(SysRoleReq sysRoleReq) {
		List<SysRoleDto> sysRoleDtos = sysRoleService.select(sysRoleReq);
		return R.success(sysRoleDtos);
	}

	/**
	 * 新添加角色数据信息
	 * 
	 * @param sysRoleInsertDto
	 * @return
	 * @throws AuthenticationException
	 */
	@PostMapping
	// @RequiresPermissions("sys:role:save")
	public Object save(@RequestBody SysRoleInsertDto sysRoleInsertDto) throws AuthenticationException {
		if (StringUtils.isBlank(sysRoleInsertDto.getName())) {
			return R.fail("创建失败,角色名不能为空...");
		}
		if (StringUtils.isNotBlank(sysRoleInsertDto.getName())) {
			int roleCount = sysRoleService.getCountByTypeAndNameOrById(sysRoleInsertDto.getName(), null);
			if (roleCount > 0) {
				return R.fail("添加角色信息失败，角色已经存在！");
			}
		}
		sysRoleService.save(sysRoleInsertDto);
		return R.success("saveRole success!");
	}

	/**
	 * 修改角色数据信息
	 * 
	 * @param sysRoleUpdateDto
	 * @return
	 * @throws AuthenticationException
	 */
	@PutMapping
	// @RequiresPermissions("sys:role:update")
	public Object update(@RequestBody SysRoleUpdateDto sysRoleUpdateDto) throws AuthenticationException {
		if (StringUtils.isNotBlank(sysRoleUpdateDto.getName())) {
			Integer roleCount = sysRoleService.getCountByTypeAndNameOrById(sysRoleUpdateDto.getName(),
					sysRoleUpdateDto.getId());
			if (null == roleCount || roleCount > 0) {
				return R.fail("修改失败信息失败，角色已经存在！");
			}
		}
		sysRoleService.update(sysRoleUpdateDto);
		return R.success("updateRole success!");
	}

	/**
	 * 删除角色数据信息
	 * 
	 * @param sysRoleReq
	 * @return
	 */
	@DeleteMapping
	// @RequiresPermissions("sys:role:delete")
	public Object delete(@RequestBody SysRoleDeleteDto sysRoleDeleteDto) {
		if (sysRoleDeleteDto.getId() == Constants.SUPER_ADMIN.intValue()) {
			return R.fail("管理员角色不可以被删除！");
		}
		List<String> queryRoleIdList = sysUserRoleService.queryUseruamesByRoleId(sysRoleDeleteDto.getId());
		if (queryRoleIdList.size() > 0) {
			return R.fail("此角色还有关联的用户没有清除干净！比如：" + queryRoleIdList);
		}
		sysRoleService.delete(sysRoleDeleteDto);
		return R.success("deleteRole success!");
	}
}