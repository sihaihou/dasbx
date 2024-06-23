package com.reyco.dasbx.model.constants;

public enum OperationType {
	CREATE("创建"),
	UPDATE("更新"),
	READ("查看"),
	DELETE("删除");
	private String name;

	OperationType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
