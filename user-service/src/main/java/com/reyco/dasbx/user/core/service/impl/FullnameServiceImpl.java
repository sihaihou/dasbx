package com.reyco.dasbx.user.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.model.domain.Fullname;
import com.reyco.dasbx.user.core.feign.FullnameClient;
import com.reyco.dasbx.user.core.service.FullnameService;

@Service
public class FullnameServiceImpl implements FullnameService{
	private static Logger logger = LoggerFactory.getLogger(FullnameServiceImpl.class);
	@Autowired
	private FullnameClient fullnameService;
	
	public Fullname randomFullName() {
		R<Fullname> r = fullnameService.randomFullName();
		if(r.getSuccess() && r.getCode()==200) {
			return r.getData();
		}
		logger.error("获取全名失败："+r.getMsg());
		return null;
	}
}
