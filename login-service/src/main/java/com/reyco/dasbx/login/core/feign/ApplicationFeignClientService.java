package com.reyco.dasbx.login.core.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.login.core.model.domain.Application;

@Service
public class ApplicationFeignClientService {
	@Autowired
	private ApplicationFeignClient applicationFeignClient;
	
	public Application getByClientId(String clientId) {
		R<Application> r = applicationFeignClient.getByClientId(clientId);
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		return null;
	}
	
}
