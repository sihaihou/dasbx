package com.reyco.dasbx.actuator.tools;

import java.util.Locale;
import java.util.function.Function;

public class ToolsIndicatorNameFactory implements Function<String, String> {
	
	@Override
	public String apply(String name) {
		int index = name.toLowerCase(Locale.ENGLISH).indexOf("toolsindicator");
		if (index > 0) {
			return name.substring(0, index);
		}
		return name;
	}
}
