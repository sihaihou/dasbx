package com.reyco.dasbx.resource.core.support;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.reyco.dasbx.desensitize.log.LogMaskingEngine;
import com.reyco.dasbx.desensitize.log.LogMaskingRule;
import com.reyco.dasbx.resource.core.support.ResourceMaskingProperties.Rule;

/**
 * 资源日志专用脱敏器。
 *
 * <p>
 * 复用 desensitize-core 的执行引擎，但使用资源日志自己的规则， 不读取 desensitize-spring-boot-starter
 * 的全局日志配置。
 * </p>
 */
public class ResourceParameterMasker {

	private static final int MAX_DEPTH = 10;

	private static final int MAX_TEXT_LENGTH = 1000;

	private static final String PHONE_EXPRESSION = "(?<!\\d)(\\+?86[- ]?)?(1[3-9]\\d)\\d{4}(\\d{4})(?!\\d)";

	private static final String ID_CARD_EXPRESSION = "(?<![0-9A-Za-z])([1-9]\\d{2})\\d{11}([0-9]{3}[0-9Xx])(?![0-9A-Za-z])";

	private static final String EMAIL_EXPRESSION = "(?<![A-Za-z0-9._%+-])([A-Za-z0-9])[A-Za-z0-9._%+-]*(@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})(?![A-Za-z0-9._%+-])";

	private static final String SENSITIVE_FIELD_EXPRESSION = "(?i)(password|passwd|pwd|token|access_token|refresh_token|authorization|secret)=(.+)$";

	/**
	 * 每个脱敏器拥有独立的不可变规则集，线程安全。
	 */
	private final LogMaskingEngine logMaskingEngine;

	private final String tokenMask;

	/**
	 * 支持测试或非 Spring 环境使用默认资源规则。
	 */
	public ResourceParameterMasker() {
		this(new ResourceMaskingProperties());
	}

	public ResourceParameterMasker(ResourceMaskingProperties properties) {
		ResourceMaskingProperties maskingProperties = properties == null ? new ResourceMaskingProperties() : properties;

		this.logMaskingEngine = new LogMaskingEngine(buildRules(maskingProperties));

		String configuredTokenMask = maskingProperties.getTokenMask();
		this.tokenMask = configuredTokenMask == null || configuredTokenMask.isEmpty() ? "******" : configuredTokenMask;
	}

	private static List<LogMaskingRule> buildRules(ResourceMaskingProperties properties) {
		List<LogMaskingRule> rules = new ArrayList<LogMaskingRule>();

		if (properties.isDefaultRulesEnabled()) {
			rules.add(new LogMaskingRule("resource-phone", PHONE_EXPRESSION, "$1$2****$3"));

			rules.add(new LogMaskingRule("resource-id-card", ID_CARD_EXPRESSION, "$1***********$2"));

			rules.add(new LogMaskingRule("resource-email", EMAIL_EXPRESSION, "$1***$2"));

			rules.add(new LogMaskingRule("resource-sensitive-field", SENSITIVE_FIELD_EXPRESSION, "$1=******"));
		}

		List<Rule> configuredRules = properties.getRules();
		if (configuredRules == null || configuredRules.isEmpty()) {
			return rules;
		}

		for (Rule rule : configuredRules) {
			if (rule == null || !rule.isEnabled()) {
				continue;
			}

			if (rule.getRegex() == null || rule.getRegex().isEmpty()) {
				throw new IllegalArgumentException("Resource masking rule regex must not be empty: " + rule.getName());
			}

			if (rule.getReplacement() == null) {
				throw new IllegalArgumentException(
						"Resource masking rule replacement must not be null: " + rule.getName());
			}

			rules.add(new LogMaskingRule(rule.getName() == null ? "resource-custom-rule" : rule.getName(),
					rule.getRegex(), rule.getReplacement()));
		}

		return rules;
	}

	public Map<String, Object> maskParameters(Map<String, Object> parameters) {
		if (parameters == null || parameters.isEmpty()) {
			return new LinkedHashMap<String, Object>();
		}

		Object masked = maskValue(null, parameters, 0,
				Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>()));

		if (!(masked instanceof Map)) {
			return new LinkedHashMap<String, Object>();
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) masked;
		return result;
	}

	public String maskToken(String token) {
	    if (token == null) {
	        return null;
	    }
	    return tokenMask;
	}

	public String maskText(String text) {
		if (text == null) {
			return null;
		}
		String masked = logMaskingEngine.mask(text);
		return masked.length() > MAX_TEXT_LENGTH ? masked.substring(0, MAX_TEXT_LENGTH) : masked;
	}

	private Object maskValue(String key, Object value, int depth, Set<Object> path) {
		if (value == null || depth > MAX_DEPTH) {
			return null;
		}

		if (value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Character
				|| value instanceof Enum<?>) {
			return maskScalar(key, value.toString());
		}

		if (value instanceof Map<?, ?>) {
			if (!path.add(value)) {
				return null;
			}
			try {
				Map<String, Object> result = new LinkedHashMap<String, Object>();
				for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
					String childKey = String.valueOf(entry.getKey());
					result.put(childKey, maskValue(childKey, entry.getValue(), depth + 1, path));
				}
				return result;
			} finally {
				path.remove(value);
			}
		}

		if (value instanceof Collection<?>) {
			if (!path.add(value)) {
				return null;
			}
			try {
				List<Object> result = new ArrayList<Object>();
				for (Object item : (Collection<?>) value) {
					result.add(maskValue(key, item, depth + 1, path));
				}
				return result;
			} finally {
				path.remove(value);
			}
		}

		if (value.getClass().isArray()) {
			List<Object> result = new ArrayList<Object>();
			for (int i = 0; i < Array.getLength(value); i++) {
				result.add(maskValue(key, Array.get(value, i), depth + 1, path));
			}
			return result;
		}

		return maskScalar(key, value.toString());
	}

	/**
	 * 使用资源日志自己的规则处理 key=value。
	 */
	private String maskScalar(String key, String value) {
		if (value == null) {
			return null;
		}

		String text = key == null ? value : key + "=" + value;
		String masked = logMaskingEngine.mask(text);

		if (key == null) {
			return trim(masked);
		}

		String prefix = key + "=";
		if (masked.startsWith(prefix)) {
			return trim(masked.substring(prefix.length()));
		}
		return trim(masked);
	}

	private String trim(String text) {
		return text.length() > MAX_TEXT_LENGTH ? text.substring(0, MAX_TEXT_LENGTH) : text;
	}
}