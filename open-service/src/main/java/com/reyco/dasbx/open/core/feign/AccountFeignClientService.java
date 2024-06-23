package com.reyco.dasbx.open.core.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.open.core.model.dto.developer.AccountBindDeveloperDto;

@Service
public class AccountFeignClientService {
	@Autowired
	private AccountFeignClient accountFeignClient;
	
	public SysAccount getByUid(String uid) {
		R<SysAccount> r = accountFeignClient.getByUid(uid);
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		return null;
	}
	
	public Boolean bindDeveloper(AccountBindDeveloperDto accountBindDeveloperDto) {
		R<String> r = accountFeignClient.bindDeveloper(accountBindDeveloperDto);
		if(r.getSuccess() && r.getCode()==200) {
			return true;
		}
		return false;
	}
}
