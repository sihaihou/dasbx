package com.reyco.dasbx.login.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.Fullname;

@FeignClient(name="common-service")
public interface FullnameFeignClient {
	
	@PostMapping("/fullname/randomFullName")
	R<Fullname> randomFullName();
	
}
