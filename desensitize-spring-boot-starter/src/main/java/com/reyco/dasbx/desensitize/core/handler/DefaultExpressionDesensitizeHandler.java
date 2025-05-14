package com.reyco.dasbx.desensitize.core.handler;

import org.springframework.util.StringUtils;

import com.reyco.dasbx.desensitize.core.DesensitizeType;

/**
 * 表达式脱敏处理器
 * @author reyco
 *
 */
public class DefaultExpressionDesensitizeHandler implements DesensitizeHandler {
	
	@Override
	public Boolean support(DesensitizeType type) {
		return type instanceof DesensitizeType.ExpressionDesensitizeType;
	}
	@Override
	public String handler(DesensitizeType type, String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		DesensitizeType.ExpressionDesensitizeType expressionDesensitizeType = (DesensitizeType.ExpressionDesensitizeType)type;
		return value.replaceAll(expressionDesensitizeType.getExpression(), expressionDesensitizeType.getReplaceExpression());
	}

}
