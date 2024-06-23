package com.reyco.dasbx.common.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.feign.SysAccountClient;
import com.reyco.dasbx.common.core.service.SysAccountService;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.SysAccount;

@Service
public class SysAccountServiceImpl implements SysAccountService {
	@Autowired
	private SysAccountClient sysAccountClient;
	
	public SysAccount getById(Long id) {
		R<SysAccount> r = sysAccountClient.getById(id);
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		return null;
	}
}
