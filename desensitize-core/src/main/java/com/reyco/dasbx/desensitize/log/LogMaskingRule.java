package com.reyco.dasbx.desensitize.log;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 一条用于自由文本日志的脱敏规则。
 */
public final class LogMaskingRule {

	private final String name;
	private final Pattern pattern;
	private final String replacement;

	public LogMaskingRule(String name, String expression, String replacement) {
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.pattern = Pattern.compile(Objects.requireNonNull(expression, "expression must not be null"));
		this.replacement = Objects.requireNonNull(replacement, "replacement must not be null");
	}

	public String getName() {
		return name;
	}

	public String apply(String text) {
		return pattern.matcher(text).replaceAll(replacement);
	}
}
