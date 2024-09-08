package com.reyco.dasbx.portal.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.SysVip;

@FeignClient(name="common-service")
public interface SysVipFeignClient {
	
	@GetMapping("/vip/list")
	R<List<SysVip>> list();
}
