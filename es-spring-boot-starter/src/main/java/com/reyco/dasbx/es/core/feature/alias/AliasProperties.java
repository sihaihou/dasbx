package com.reyco.dasbx.es.core.feature.alias;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "reyco.dasbx.es.alias")
public class AliasProperties {
	/**
	 * alias配置
	 */
	private List<AliasDefinition> definitions = new ArrayList<>();

	public List<AliasDefinition> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<AliasDefinition> definitions) {
		this.definitions = definitions;
	}

}
