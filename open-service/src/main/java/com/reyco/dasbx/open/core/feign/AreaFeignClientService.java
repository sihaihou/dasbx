package com.reyco.dasbx.open.core.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.Area;

@Service
public class AreaFeignClientService {
	@Autowired
	private AreaFeignClient areaFeignClient;
	
	public Area getById(Long id) {
		R<Area> r = areaFeignClient.getById(id);
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		return null;
	}
}
