package com.reyco.dasbx.es.core.query.score;

import java.util.HashMap;
import java.util.Map;

public class ScriptScoreFunction implements ScoreFunction {

	/**
	 * 脚本
	 */
	private String script;
	/**
	 * 参数
	 */
	private Map<String, Object> params = new HashMap<>();
	/**
	 * 语言
	 */
	private String lang = "painless";

	public ScriptScoreFunction(String script) {
		this.script = script;
	}

	/**
	 * 参数
	 */
	public ScriptScoreFunction param(String key, Object value) {
		params.put(key, value);
		return this;
	}

	/**
	 * 脚本语言
	 */
	public ScriptScoreFunction lang(String lang) {
		this.lang = lang;
		return this;
	}

	public String getScript() {
		return script;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public String getLang() {
		return lang;
	}

}
