package com.reyco.dasbx.portal.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.SysAccount;

@Service
public class AccountFeignClientService {
	
	@Autowired
	private AccountFeignClient accountFeignClient;
	
	public SysAccount getById(Long id) {
		R<SysAccount> r = accountFeignClient.getById(id);
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		return null;
	}

}
