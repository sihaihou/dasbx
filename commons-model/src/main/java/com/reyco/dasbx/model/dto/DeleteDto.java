package com.reyco.dasbx.model.dto;

public interface DeleteDto extends BaseDto {
	
	Long getId();
	
	Byte getDeleted();
	
	Long getGmtModified();
	
	Long getModifiedBy();
}
