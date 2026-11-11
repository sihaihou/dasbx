package com.reyco.dasbx.desensitize.log;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = LogMaskingProperties.PREFIX)
public class LogMaskingProperties {

	public static final String PREFIX = "reyco.dasbx.desensitize.log";

	private boolean enabled = true;
	private List<Rule> rules = new ArrayList<>();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
	}
}
