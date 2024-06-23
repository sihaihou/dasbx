package com.reyco.dasbx.user.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.Fullname;

@FeignClient("common-service")
public interface FullnameClient {
	
	@GetMapping("/fullname/randomFullName")
	public R<Fullname> randomFullName();
	
}
