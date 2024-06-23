package com.reyco.dasbx.portal.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.SysAccount;

@FeignClient(name="user-service")
public interface AccountFeignClient {
	
	@GetMapping("/sys/account/{id}")
	R<SysAccount> getById(@PathVariable("id")Long id);
}
