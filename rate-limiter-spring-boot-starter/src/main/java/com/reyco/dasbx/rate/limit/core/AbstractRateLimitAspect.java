package com.reyco.dasbx.rate.limit.core;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

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

	/**
	 * 验证时间单位
	 * @param unit
	 */
	protected void validateTimeUnit(TimeUnit unit) {
		if (unit == null) {
		    throw new IllegalArgumentException("限流时间单位不能为空");
		}
		switch (unit) {
		case MILLISECONDS:
		case SECONDS:
		case MINUTES:
		case HOURS:
		case DAYS:
			return;
		case MICROSECONDS:
		case NANOSECONDS:
			throw new IllegalArgumentException("不支持 " + unit + " 级别限流，请使用 MILLISECONDS、SECONDS、MINUTES、HOURS 或 DAYS");
		default:
			throw new IllegalArgumentException("不支持的时间单位: " + unit);
		}
	}
	/**
	 * 格式化时间窗口显示
	 */
	protected String formatWindow(int window, TimeUnit unit) {
		long remainingMillis = unit.toMillis(window);

	    long days = remainingMillis / 86_400_000L;
	    remainingMillis %= 86_400_000L;

	    long hours = remainingMillis / 3_600_000L;
	    remainingMillis %= 3_600_000L;

	    long minutes = remainingMillis / 60_000L;
	    remainingMillis %= 60_000L;

	    long seconds = remainingMillis / 1_000L;
	    long milliseconds = remainingMillis % 1_000L;

	    StringBuilder result = new StringBuilder();
	    if (days > 0) {
	        result.append(days).append("天");
	    }
	    if (hours > 0) {
	        result.append(hours).append("小时");
	    }
	    if (minutes > 0) {
	        result.append(minutes).append("分钟");
	    }
	    if (seconds > 0) {
	        result.append(seconds).append("秒");
	    }
	    if (milliseconds > 0) {
	        result.append(milliseconds).append("毫秒");
	    }
	    return result.toString();
	}
}
