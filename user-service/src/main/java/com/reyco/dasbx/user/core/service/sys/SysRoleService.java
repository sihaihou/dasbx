package com.reyco.dasbx.user.core.service.sys;

import java.io.IOException;
import java.util.List;

import com.reyco.dasbx.es.core.search.SearchVO;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleDeleteDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleInsertDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleSearchDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleUpdateDto;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleReq;
import com.reyco.dasbx.user.core.model.vo.sys.SysRoleInfoVO;

/**
 * 权限管理信息
 * @author reyco
 *
 */
public interface SysRoleService {
	
	Integer initElasticsearchSysRole() throws IOException;
	
    /**
     * 根据id获取用户信息
     *
     * @param roleId
     * @return
     */
    SysRoleInfoVO get(Long roleId);
    
    List<String> getSuggestion(String keyword) throws Exception;
    /**
     * 分页查询
     *
     * @param sysRolePageDto
     * @return
     */
    SearchVO<SysRoleInfoVO> search(SysRoleSearchDto sysRoleSearchDto) throws IOException;
    
    /**
     * 列表查询
     *
     * @param sysRoleReq
     * @return
     */
    List<SysRoleDto> select(SysRoleReq sysRoleReq);

    /**
     * 修改用户基本信息
     *
     * @param sysRoleUpdateDto
     */
    void update(SysRoleUpdateDto sysRoleUpdateDto);

    /**
     * 创建用户
     *
     * @param sysRoleInsertDto
     */
    void save(SysRoleInsertDto sysRoleInsertDto);

    /**
     * 根据名称或者非id查询count
     *
     * @param username
     * @param userId
     * @return
     */
    Integer getCountByTypeAndNameOrById(String username, Long userId);

    /**
     * 删除角色数据信息
     * @param roleId
     */
    void delete(SysRoleDeleteDto sysRoleDeleteDto);
}