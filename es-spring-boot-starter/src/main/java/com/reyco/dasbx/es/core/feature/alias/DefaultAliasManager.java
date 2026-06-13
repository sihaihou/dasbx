package com.reyco.dasbx.es.core.feature.alias;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAliasManager implements AliasManager {

	/**
	 * alias缓存
	 */
	private final Map<String, AliasDefinition> aliasMap = new ConcurrentHashMap<>();

	public DefaultAliasManager(AliasProperties properties) {
		if (properties == null) {
			return;
		}
		List<AliasDefinition> definitions = properties.getDefinitions();
		if (definitions == null) {
			return;
		}
		for (AliasDefinition definition : definitions) {
			aliasMap.put(definition.getAlias(), definition);
		}
	}

	@Override
	public void register(String alias, List<String> indices) {
		AliasDefinition definition = new AliasDefinition();
		definition.setAlias(alias);
		definition.setIndices(indices);

		aliasMap.put(alias, definition);
	}

	@Override
	public List<String> resolve(String alias) {
		AliasDefinition definition = aliasMap.get(alias);
		//不存在alias
		if (definition == null) {
			return Collections.singletonList(alias);
		}

		//pattern alias
		if (definition.getPattern() != null && !definition.getPattern().isEmpty()) {
			return Collections.singletonList(definition.getPattern());
		}

		//static indices
		List<String> indices = definition.getIndices();
		if (indices == null || indices.isEmpty()) {
			return Collections.singletonList(alias);
		}

		return indices;
	}
	
	@Override
	public AliasDefinition get(String alias) {
		return aliasMap.get(alias);
	}

	@Override
	public boolean exists(String alias) {
		return aliasMap.containsKey(alias);
	}
	
}
