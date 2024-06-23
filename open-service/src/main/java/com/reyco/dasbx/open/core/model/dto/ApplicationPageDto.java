package com.reyco.dasbx.open.core.model.dto;

import com.reyco.dasbx.model.dto.SimplePageDto;
import com.reyco.dasbx.open.core.model.po.ApplicationSelectPO;

public class ApplicationPageDto extends SimplePageDto{
	
	private ApplicationSelectPO applicationSelectPO;

	public ApplicationSelectPO getApplicationSelectPO() {
		return applicationSelectPO;
	}

	public void setApplicationSelectPO(ApplicationSelectPO applicationSelectPO) {
		this.applicationSelectPO = applicationSelectPO;
	}
}
