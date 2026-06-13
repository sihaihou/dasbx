package com.reyco.dasbx.rate.limit.core;

import java.util.StringJoiner;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

public class AbstractRateLimitAspect {
	
	private final ExpressionParser spelExpressionParser = new SpelExpressionParser();
	
	private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

	/**
	 * 统一构建限流 Key 的核心公共方法
	 */
	protected String getKey(ProceedingJoinPoint joinPoint, String prefix, String value, String keyExpress) {
		if (!StringUtils.hasText(prefix) || !StringUtils.hasText(value)) {
			throw new IllegalArgumentException("限流注解的 [prefix] 和 [value] 属性不能为空！");
		}

		StringJoiner joiner = new StringJoiner(":");
		joiner.add(prefix);
		joiner.add(value);

		// 解析动态 SpEL 的 Value
		String dynamicValue = getValue(joinPoint, keyExpress);
		if (StringUtils.hasText(dynamicValue)) {
			joiner.add(dynamicValue);
		}
		return joiner.toString();
	}

	/**
	 * 统一解析 SpEL
	 */
	private String getValue(ProceedingJoinPoint joinPoint, String keyExpress) {
		if (!StringUtils.hasText(keyExpress)) {
			return "";
		}
		if (!keyExpress.startsWith("#")) {
			return keyExpress;
		}
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Expression expression = spelExpressionParser.parseExpression(keyExpress);
		EvaluationContext evaluationContext = new MethodBasedEvaluationContext(joinPoint.getTarget(),
				signature.getMethod(), joinPoint.getArgs(), parameterNameDiscoverer);
		return expression.getValue(evaluationContext, String.class);
	}
}
