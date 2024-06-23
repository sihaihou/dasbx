package com.reyco.dasbx.login.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.id.core.IdGenerator;
import com.reyco.dasbx.login.core.service.SysService;

@Service
public class SysServiceImpl implements SysService {

	@Autowired
	private IdGenerator<Long> idGenerator;
	@Override
	public Long getCode(Long code) {
		if(code==null) {
			code = idGenerator.getGeneratorId();
		}
		return code;
	}
}
