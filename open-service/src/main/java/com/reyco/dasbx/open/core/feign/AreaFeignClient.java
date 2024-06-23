package com.reyco.dasbx.open.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.Area;

@FeignClient(name="common-service")
public interface AreaFeignClient {
	
	@GetMapping("/sys/area/{id}")
	R<Area> getById(@PathVariable("id")Long id);
	
	
}
