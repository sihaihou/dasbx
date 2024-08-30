package com.reyco.dasbx.portal.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;

@FeignClient(name="common-service")
public interface SysMessageFeignClient {
	
	@PostMapping("/sys/message/add")
	R<String> add(@RequestBody SysMessageInsertDto sysMessageInsertDto);
}
