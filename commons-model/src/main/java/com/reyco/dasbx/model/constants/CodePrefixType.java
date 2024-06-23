package com.reyco.dasbx.model.constants;

public enum CodePrefixType {
	AREA("区域编号前缀","",8),
	PERSONAGE("人物编号前缀","PO",8);
	private String name;
	private String prefix;
	private int length;
	
	CodePrefixType(String name,String prefix,int length) {
		this.name = name;
		this.prefix=prefix;
		this.length=length;
	}
	public static CodePrefixType getCodePrefixType(String name) {
		for (CodePrefixType codePrefixType : CodePrefixType.values()) {
			if (codePrefixType.name.equals(name)) {
				return codePrefixType;
			}
		}
		return null;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
