package com.reyco.dasbx.rate.limit.core;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.reyco.dasbx.lua.core.LuaScriptTemplate;
import com.reyco.dasbx.rate.limit.annotation.SlidingWindowRateLimit;
import com.reyco.dasbx.rate.limit.exception.RateLimitException;

@Aspect
public class SlidingWindowRateLimitAspect extends AbstractRateLimitAspect {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final static SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

	private final static DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

	private LuaScriptTemplate luaScriptTemplate;
	
	public SlidingWindowRateLimitAspect() {
	}

	public SlidingWindowRateLimitAspect(LuaScriptTemplate luaScriptTemplate) {
		super();
		this.luaScriptTemplate = luaScriptTemplate;
	}
	public void setLuaScriptTemplate(LuaScriptTemplate luaScriptTemplate) {
		this.luaScriptTemplate = luaScriptTemplate;
	}

	@Around("@annotation(com.reyco.dasbx.rate.limit.annotation.SlidingWindowRateLimit)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		SlidingWindowRateLimit slidingWindowRateLimit = AnnotatedElementUtils.getMergedAnnotation(
	            signature.getMethod(), SlidingWindowRateLimit.class);
		if (slidingWindowRateLimit != null) {
			// 1. 构建限流key
			String rateLimitKey = getKey(joinPoint,slidingWindowRateLimit.prefix(),slidingWindowRateLimit.value(),slidingWindowRateLimit.key());

			// 2. 获取限流参数
			int window = slidingWindowRateLimit.window();
			TimeUnit unit = slidingWindowRateLimit.unit();
			int maxRequests = slidingWindowRateLimit.maxRequests();

			// 3. 参数校验 - 只支持秒、分钟、小时、天
			validateTimeUnit(unit);

			// 4. 将时间窗口转换为毫秒
			long windowMillis = unit.toMillis(window);

			// 5. 当前时间戳（毫秒）和请求ID
			long now = System.currentTimeMillis();
			String requestId = now + ":" + Thread.currentThread().getId() + ":" + UUID.randomUUID().toString();

			// 6. 执行限流检查
			List<String> keys = Arrays.asList(rateLimitKey);
			Long result = luaScriptTemplate.executeScript("SlidingWindowRateLimit", keys, String.valueOf(windowMillis),
					String.valueOf(maxRequests), String.valueOf(now), requestId);
			
			if (result == 1L) {
				logger.debug("Sliding window rate limit passed - key: {}, window: {}, maxRequests: {}", 
						rateLimitKey, formatWindow(window, unit), maxRequests);
				return joinPoint.proceed();
			} else {
				logger.warn("Sliding window rate limit blocked - key: {}, window: {}, maxRequests: {}", 
						rateLimitKey, formatWindow(window, unit), maxRequests);
		            throw new RateLimitException("请求过于频繁，请" + formatWindow(window, unit) + "后再试");
			}
		}
		return joinPoint.proceed();
	}

	/**
	 * 获取Key
	 * 
	 * @param joinPoint
	 * @return
	 */
	@Deprecated
	protected String getKey(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		SlidingWindowRateLimit slidingWindowRateLimit = AnnotatedElementUtils.getMergedAnnotation(
	            signature.getMethod(), SlidingWindowRateLimit.class);
		String value = getValue(joinPoint);
		if (StringUtils.isBlank(slidingWindowRateLimit.prefix())
				|| StringUtils.isBlank(slidingWindowRateLimit.value())) {
			throw new RuntimeException("@SlidingWindowRateLimit annotation configuration error,@SlidingWindowRateLimit:"
					+ slidingWindowRateLimit);
		}
		StringJoiner joiner = new StringJoiner(":");
		joiner.add(slidingWindowRateLimit.prefix());
		joiner.add(slidingWindowRateLimit.value());
		if (StringUtils.isNotBlank(value)) {
			joiner.add(value);
		}
		return joiner.toString();
	}

	/**
	 * 获取Value
	 * 
	 * @param joinPoint
	 * @return
	 */
	@Deprecated
	private String getValue(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		SlidingWindowRateLimit slidingWindowRateLimit = AnnotatedElementUtils.getMergedAnnotation(
	            signature.getMethod(), SlidingWindowRateLimit.class);
		String key = slidingWindowRateLimit.key();
		if (StringUtils.isBlank(key)) {
			return "";
		}
		if (!key.startsWith("#")) {
			return key;
		}
		Expression expression = spelExpressionParser.parseExpression(key);
		EvaluationContext evaluationContext = new MethodBasedEvaluationContext(joinPoint.getTarget(), signature.getMethod(),
				joinPoint.getArgs(), parameterNameDiscoverer);
		return expression.getValue(evaluationContext, String.class);
	}
	/**
	 * 验证时间单位
	 * @param unit
	 */
	private void validateTimeUnit(TimeUnit unit) {
		switch (unit) {
		case SECONDS:
		case MINUTES:
		case HOURS:
		case DAYS:
			return;
		case MILLISECONDS:
		case MICROSECONDS:
		case NANOSECONDS:
			throw new IllegalArgumentException("不支持 " + unit + " 级别限流，请使用 SECONDS、MINUTES、HOURS 或 DAYS");
		default:
			throw new IllegalArgumentException("不支持的时间单位: " + unit);
		}
	}

	/**
	 * 格式化时间窗口显示
	 */
	private String formatWindow(int window, TimeUnit unit) {
		switch (unit) {
		case SECONDS:
			if (window < 60) {
				return window + "秒";
			} else if (window == 60) {
				return "1分钟";
			} else if (window % 60 == 0) {
				return (window / 60) + "分钟";
			} else {
				int minutes = window / 60;
				int seconds = window % 60;
				return minutes + "分钟" + seconds + "秒";
			}
		case MINUTES:
			if (window == 1) {
				return "1分钟";
			} else if (window < 60) {
				return window + "分钟";
			} else if (window == 60) {
				return "1小时";
			} else {
				int hours = window / 60;
				int minutes = window % 60;
				if (minutes == 0) {
					return hours + "小时";
				} else {
					return hours + "小时" + minutes + "分钟";
				}
			}
		case HOURS:
			if (window == 1) {
				return "1小时";
			} else if (window < 24) {
				return window + "小时";
			} else if (window == 24) {
				return "1天";
			} else {
				int days = window / 24;
				int hours = window % 24;
				if (hours == 0) {
					return days + "天";
				} else {
					return days + "天" + hours + "小时";
				}
			}
		case DAYS:
			return window + "天";
		default:
			 return window + unit.name().toLowerCase();
		}
	}

}
