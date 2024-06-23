package com.reyco.dasbx.user.core.service.sys;

import java.io.IOException;
import java.util.List;

import com.reyco.dasbx.config.exception.core.BusinessException;
import com.reyco.dasbx.config.service.BaseService;
import com.reyco.dasbx.es.core.search.ElasticsearchVO;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.user.core.model.dto.AccountBindDeveloperDto;
import com.reyco.dasbx.user.core.model.dto.AccountListDto;
import com.reyco.dasbx.user.core.model.dto.SysAccountInsertDto;
import com.reyco.dasbx.user.core.model.dto.SysAccountRegisterDto;
import com.reyco.dasbx.user.core.model.dto.SysAccountUpdateDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysAccountDeleteDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysAccountDisableOrEnableDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysAccountSearchDto;
import com.reyco.dasbx.user.core.model.vo.AccountListVO;
import com.reyco.dasbx.user.core.model.vo.SysAccountInfoVO;

public interface SysAccountService extends BaseService<SysAccountInfoVO, AccountListVO, AccountListDto, SysAccountUpdateDto, SysAccountInsertDto, SysAccountDeleteDto>{
	
	SysAccountInfoVO currentUser() throws Exception;
	
	SysAccountInfoVO getByUid(String uid);
	
	SysAccountInfoVO getByEmail(String email);
	
	SysAccount getByUsername(String username);
	 /**
     * 根据名称或者非id查询count
     * @param userId
     * @return
     */
    int getCountByTypeAndNameOrById(String username, Long userId);
	
    List<String> getSuggestion(String keyword) throws Exception;
    
    ElasticsearchVO<SysAccountInfoVO> search(SysAccountSearchDto sysAccountSearchDto) throws IOException;
	
	int initElasticsearchSysAccount() throws IOException;
	/**
	 * 禁用启用
	 * @param SysAccountDisableOrEnableDto
	 */
	void updateState(SysAccountDisableOrEnableDto sysAccountDisableOrEnableDto) ;
	/**
	 * 注册
	 * @param sysAccountRegisterDto
	 * @return
	 */
	void register(SysAccountRegisterDto sysAccountRegisterDto);
	
	/**
	 * 绑定开发者
	 * @param accountBindDeveloperDto
	 */
	void bindDeveloper(AccountBindDeveloperDto accountBindDeveloperDto) throws BusinessException;
	
}
