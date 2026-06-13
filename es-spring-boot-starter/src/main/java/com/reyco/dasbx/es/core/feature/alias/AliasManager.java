package com.reyco.dasbx.es.core.feature.alias;

import java.util.List;

public interface AliasManager {
	
	/**
	 * 注册alias
	 */
	void register(String alias, List<String> indices);
	
	/**
	 * 解析alias
	 */
	List<String> resolve(String alias);
	/**
	 * 获取AliasDefinition
	 * @param alias
	 * @return
	 */
	AliasDefinition get(String alias);

	/**
	 * 是否存在
	 */
	boolean exists(String alias);
	
}
