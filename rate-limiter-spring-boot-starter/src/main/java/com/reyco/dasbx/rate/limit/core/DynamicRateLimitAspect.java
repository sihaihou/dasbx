package com.reyco.dasbx.rate.limit.core;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;

import com.reyco.dasbx.lua.core.LuaScriptTemplate;
import com.reyco.dasbx.rate.limit.annotation.DynamicRateLimit;
import com.reyco.dasbx.rate.limit.exception.RateLimitException;

@Aspect
public class DynamicRateLimitAspect extends AbstractRateLimitAspect{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private DynamicRateLimitService dynamicRateLimitService;
	
	private LuaScriptTemplate luaScriptTemplate;
	
	public DynamicRateLimitAspect() {
		// TODO Auto-generated constructor stub
	}
	public DynamicRateLimitAspect(DynamicRateLimitService dynamicRateLimitService, LuaScriptTemplate luaScriptTemplate) {
		super();
		this.dynamicRateLimitService = dynamicRateLimitService;
		this.luaScriptTemplate = luaScriptTemplate;
	}
	public void setLuaScriptTemplate(LuaScriptTemplate luaScriptTemplate) {
		this.luaScriptTemplate = luaScriptTemplate;
	}
	public void setDynamicRateLimitService(DynamicRateLimitService dynamicRateLimitService) {
		this.dynamicRateLimitService = dynamicRateLimitService;
	}
	
	@Around("@annotation(com.reyco.dasbx.rate.limit.annotation.DynamicRateLimit)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		DynamicRateLimit dynamicRateLimit = AnnotatedElementUtils.getMergedAnnotation(
	            signature.getMethod(), DynamicRateLimit.class);
		if (dynamicRateLimit == null) {
			return joinPoint.proceed();
		}
		// 1. 解析限流key
		String rateLimitKey = getKey(joinPoint,dynamicRateLimit.resources(),dynamicRateLimit.value(),dynamicRateLimit.key());

		// 2. 获取限流配置
		RateLimitConfig config = dynamicRateLimitService.getConfig(dynamicRateLimit.resources());
		int window = config.getWindow();
		TimeUnit unit = config.getUnit();
		int maxRequests = config.getMaxRequests();
		
		if (window <= 0) {
		    throw new IllegalArgumentException("限流窗口必须大于 0");
		}
		if (maxRequests <= 0) {
		    throw new IllegalArgumentException("窗口内最大请求次数必须大于 0");
		}

		// 3. 参数校验 - 只支持毫秒、秒、分钟、小时、天
		validateTimeUnit(unit);
		
		// 4. 将时间窗口转换为毫秒
		long windowMillis = unit.toMillis(window);
		
		// 生成请求唯一标识
		String requestId = Thread.currentThread().getId() + ":" + UUID.randomUUID().toString();
					

		// 6. 执行限流检查
		List<String> keys = Arrays.asList(rateLimitKey);
		Long result = luaScriptTemplate.executeScript("SlidingWindowRateLimit", keys, String.valueOf(windowMillis),
				String.valueOf(maxRequests), requestId);

		if (Long.valueOf(1L).equals(result)) {
			logger.debug("Sliding window rate limit passed - key: {}, window: {}, maxRequests: {}", 
					rateLimitKey, formatWindow(window, unit), maxRequests);
			return joinPoint.proceed();
		} else {
			logger.warn("Sliding window rate limit blocked - key: {}, window: {}, maxRequests: {}", 
					rateLimitKey, formatWindow(window, unit), maxRequests);
	        throw new RateLimitException("请求过于频繁，请" + formatWindow(window, unit) + "后再试");
		}
	}
	
}
