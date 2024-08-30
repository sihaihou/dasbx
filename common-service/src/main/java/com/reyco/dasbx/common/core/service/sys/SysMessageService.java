package com.reyco.dasbx.common.core.service.sys;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageDeleteDto;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageSelectDto;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageUpdateHandleDto;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageUpdateReadDto;
import com.reyco.dasbx.common.core.model.vo.sys.message.SysMessageInfoVO;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;

public interface SysMessageService {

	SysMessageInfoVO getById(Long id)throws AuthenticationException;

	PageInfo<SysMessageInfoVO> list(SysMessageSelectDto sysMessageSelectDto)throws AuthenticationException ;
	
	Integer getMessageCount() throws AuthenticationException;

	void insert(SysMessageInsertDto sysMessageInsertDto);

	void updateRead(SysMessageUpdateReadDto sysMessageUpdateReadDto) throws AuthenticationException ;

	void updateHandle(SysMessageUpdateHandleDto sysMessageUpdateHandleDto) throws AuthenticationException ;

	void deleteById(SysMessageDeleteDto sysMessageDeleteDto);
}
