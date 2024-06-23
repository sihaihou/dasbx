package com.reyco.dasbx.model.po;

public interface InsertPO extends BasePO  {
	
	Long getId();
	
	String getRemark();
	
	Long getCreateBy();
	
	Long getGmtCreate();
	
	Long getModifiedBy();
	
	Long getGmtModified();
	
	Byte getDeleted();
}
