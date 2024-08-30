package com.reyco.dasbx.user.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;

@FeignClient(name="common-service")
public interface SysMessageFeignClient {
	
	@GetMapping("/sys/message/add")
	R<String> add(@RequestBody SysMessageInsertDto sysMessageInsertDto);
}
