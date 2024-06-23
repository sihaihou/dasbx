package com.reyco.dasbx.login.core.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.Fullname;

@Service
public class FullnameFeignClientService {
	@Autowired
	private FullnameFeignClient fullnameFeignClient;
	
	public Fullname randomFullName() {
		R<Fullname> r = fullnameFeignClient.randomFullName();
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		return null;
	}
	
}
