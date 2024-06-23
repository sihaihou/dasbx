package com.reyco.dasbx.open.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.open.core.model.dto.developer.AccountBindDeveloperDto;

@FeignClient(name="user-service")
public interface AccountFeignClient {
	
	@GetMapping("/sys/account/getByUid")
	R<SysAccount> getByUid(@RequestParam("uid") String uid);
	
	@PatchMapping("/sys/account/bindDeveloper")
	R<String> bindDeveloper(@RequestBody AccountBindDeveloperDto accountBindDeveloperDto);
	
}
