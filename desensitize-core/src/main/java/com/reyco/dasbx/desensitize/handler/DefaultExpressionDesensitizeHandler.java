package com.reyco.dasbx.desensitize.handler;

import com.reyco.dasbx.desensitize.DesensitizeType;

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
		if (value == null || value.isEmpty()) {
		    return null;
		}
		DesensitizeType.ExpressionDesensitizeType expressionDesensitizeType = (DesensitizeType.ExpressionDesensitizeType)type;
		return value.replaceAll(expressionDesensitizeType.getExpression(), expressionDesensitizeType.getReplaceExpression());
	}

}
