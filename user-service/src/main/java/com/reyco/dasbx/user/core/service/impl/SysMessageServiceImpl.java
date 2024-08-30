package com.reyco.dasbx.user.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.BusinessException;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;
import com.reyco.dasbx.user.core.feign.SysMessageFeignClient;
import com.reyco.dasbx.user.core.service.SysMessageService;

@Service
public class SysMessageServiceImpl implements SysMessageService {
	
	@Autowired
	private SysMessageFeignClient sysMessageFeignClient;

	@Override
	public void insert(SysMessageInsertDto sysMessageInsertDto) throws BusinessException {
		R<String> r = sysMessageFeignClient.add(sysMessageInsertDto);
		if(!r.getSuccess() || r.getCode()!=200) {
			throw new BusinessException("消息添加失败："+r.getMsg());
		}
	}
	
}
