package com.reyco.dasbx.login.core.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.SysAccount;

@Service
public class AccountFeignClientService{
	@Autowired
	private AccountFeignClient accountFeignClient;
	
	public SysAccount getByUsername(String username) {
		R<SysAccount> r = accountFeignClient.getByUsername(username);
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		return null;
	}
	
	public SysAccount getByUid(String uid) {
		R<SysAccount> r = accountFeignClient.getByUid(uid);
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		return null;
	}
	public SysAccount getByEmail(String email) {
		R<SysAccount> r = accountFeignClient.getByEmail(email);
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		return null;
	}

}
