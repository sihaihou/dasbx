package com.reyco.dasbx.es.core.metadata;

public enum FieldType {

	KEYWORD("keyword"),

	TEXT("text"),

	LONG("long"),

	INTEGER("integer"),

	DOUBLE("double"),

	BOOLEAN("boolean"),

	DATE("date"),

	NESTED("nested"),

	OBJECT("object"),

	GEO_POINT("geo_point");

	private final String type;

	FieldType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
