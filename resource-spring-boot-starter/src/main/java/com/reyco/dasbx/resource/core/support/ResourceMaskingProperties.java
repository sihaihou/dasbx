package com.reyco.dasbx.resource.core.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = ResourceMaskingProperties.PREFIX)
public class ResourceMaskingProperties {

	public static final String PREFIX = "reyco.dasbx.resource.masking";

	/**
	 * 是否同时启用资源项目内置规则。
	 */
	private boolean defaultRulesEnabled = true;

	/**
	 * Token 字段固定使用的掩码。
	 */
	private String tokenMask = "******";

	/**
	 * 资源日志额外规则。
	 */
	private List<Rule> rules = new ArrayList<Rule>();

	public boolean isDefaultRulesEnabled() {
		return defaultRulesEnabled;
	}

	public void setDefaultRulesEnabled(boolean defaultRulesEnabled) {
		this.defaultRulesEnabled = defaultRulesEnabled;
	}

	public String getTokenMask() {
		return tokenMask;
	}

	public void setTokenMask(String tokenMask) {
		this.tokenMask = tokenMask;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public static class Rule {

		private String name;

		private String regex;

		private String replacement;

		private boolean enabled = true;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getRegex() {
			return regex;
		}

		public void setRegex(String regex) {
			this.regex = regex;
		}

		public String getReplacement() {
			return replacement;
		}

		public void setReplacement(String replacement) {
			this.replacement = replacement;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}
}