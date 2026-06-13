package com.reyco.dasbx.es.core.feature.highlight;

public class Highlights {
	
	public static HighlightDefinition highlight(String field) {
		return new HighlightDefinition(field);
	}
	
	public static HighlightDefinition highlight(String field,String preTag,String postTag) {
		return new HighlightDefinition(field);
	}
}
