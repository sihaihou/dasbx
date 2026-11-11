package com.reyco.dasbx.desensitize.log;

import java.util.ArrayList;
import java.util.List;

import com.reyco.dasbx.desensitize.log.LogMaskingProperties.Rule;

public final class LogMaskingEngineFactory {

	private LogMaskingEngineFactory() {
	}

	public static LogMaskingEngine create(LogMaskingProperties properties) {
		List<Rule> configuredRules = properties.getRules();
		if (configuredRules == null || configuredRules.isEmpty()) {
			return new LogMaskingEngine();
		}

		List<LogMaskingRule> rules = new ArrayList<>();
		for (Rule rule : configuredRules) {
			String replacement = rule.getReplacement() == null ? "******" : rule.getReplacement();
			rules.add(new LogMaskingRule(rule.getName(), rule.getRegex(), replacement));
		}
		return new LogMaskingEngine(rules);
	}
}
