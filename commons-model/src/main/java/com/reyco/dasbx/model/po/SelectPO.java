package com.reyco.dasbx.model.po;

/**
 * 列表查询接口
 * @author reyco
 *
 */
public interface SelectPO extends BasePO {
	
	default Long getId() {
		return null;
	}
	
	default Long getStartGmtCreate() {
		return null;
	}
	
	default Long getEndGmtCreate() {
		return null;
	}
	
	default Long getStartGmtModified(){
		return null;
	}
	default Long getEndGmtModified(){
		return null;
	}
}
