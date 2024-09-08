package com.reyco.dasbx.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.SysVip;
import com.reyco.dasbx.portal.feign.SysVipFeignClient;
import com.reyco.dasbx.portal.service.SysVipService;

@Service
public class SysVipServiceImpl implements SysVipService{
	
	@Autowired
	private SysVipFeignClient sysVipFeignClient;
	
	public List<SysVip> list() {
		R<List<SysVip>> r = sysVipFeignClient.list();
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		return null;
	}

}
