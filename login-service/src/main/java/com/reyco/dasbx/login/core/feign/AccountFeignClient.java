package com.reyco.dasbx.login.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.SysAccount;

@FeignClient(name="user-service")
public interface AccountFeignClient {
	
	@GetMapping("/sys/account/getByUsername")
	R<SysAccount> getByUsername(@RequestParam("username") String username);
	
	@GetMapping("/sys/account/getByUid")
	R<SysAccount> getByUid(@RequestParam("uid") String uid);
	
	@GetMapping("/sys/account/getByEmail")
	R<SysAccount> getByEmail(@RequestParam("email") String email);
}
