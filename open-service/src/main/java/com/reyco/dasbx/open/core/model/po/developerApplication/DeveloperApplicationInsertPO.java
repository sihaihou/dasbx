package com.reyco.dasbx.open.core.model.po.developerApplication;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class DeveloperApplicationInsertPO extends SimpleInsertPO{
	private Long developerId;
	private Long applicationId;
	public Long getDeveloperId() {
		return developerId;
	}
	public void setDeveloperId(Long developerId) {
		this.developerId = developerId;
	}
	public Long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
}
