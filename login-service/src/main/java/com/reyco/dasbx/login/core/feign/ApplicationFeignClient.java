package com.reyco.dasbx.login.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.login.core.model.domain.Application;

@FeignClient(name="open-service")
public interface ApplicationFeignClient {
	
	@GetMapping("/application/getByClientId")
	R<Application> getByClientId(@RequestParam("clientId")String clientId);
	
}
