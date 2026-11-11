package com.reyco.dasbx.desensitize.log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 对自由文本日志进行敏感信息扫描和脱敏。
 *
 * <p>规则在创建时编译，可在多线程环境中作为单例复用。</p>
 */
public class LogMaskingEngine {

	private static final String PHONE_EXPRESSION =
			"(?<!\\d)(\\+?86[- ]?)?(1[3-9]\\d)\\d{4}(\\d{4})(?!\\d)";

	private static final String ID_CARD_EXPRESSION =
			"(?<![0-9A-Za-z])([1-9]\\d{2})\\d{11}([0-9]{3}[0-9Xx])(?![0-9A-Za-z])";

	private static final String EMAIL_EXPRESSION =
			"(?<![A-Za-z0-9._%+-])([A-Za-z0-9])[A-Za-z0-9._%+-]*(@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})(?![A-Za-z0-9._%+-])";

	private static final List<LogMaskingRule> DEFAULT_RULES = Collections.unmodifiableList(Arrays.asList(
			new LogMaskingRule("phone", PHONE_EXPRESSION, "$1$2****$3"),
			new LogMaskingRule("id-card", ID_CARD_EXPRESSION, "$1***********$2"),
			new LogMaskingRule("email", EMAIL_EXPRESSION, "$1***$2")));

	private final List<LogMaskingRule> rules;

	public LogMaskingEngine() {
		this(DEFAULT_RULES);
	}

	public LogMaskingEngine(List<LogMaskingRule> rules) {
		this.rules = Collections.unmodifiableList(
				Objects.requireNonNull(rules, "rules must not be null"));
	}

	public String mask(String text) {
		if (text == null || text.isEmpty()) {
			return text;
		}

		String maskedText = text;
		for (LogMaskingRule rule : rules) {
			maskedText = rule.apply(maskedText);
		}
		return maskedText;
	}
}
