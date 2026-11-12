package com.reyco.dasbx.rate.limit.core;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;

import com.reyco.dasbx.lua.core.LuaScriptTemplate;
import com.reyco.dasbx.rate.limit.annotation.TokenBucketRateLimit;
import com.reyco.dasbx.rate.limit.exception.RateLimitException;

/**
 * 令牌桶限流
 * @author reyco
 *
 */
@Aspect
public class TokenBucketRateLimitAspect extends AbstractRateLimitAspect {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private LuaScriptTemplate luaScriptTemplate;
	public TokenBucketRateLimitAspect() {
		// TODO Auto-generated constructor stub
	}
	public TokenBucketRateLimitAspect(LuaScriptTemplate luaScriptTemplate) {
		super();
		this.luaScriptTemplate = luaScriptTemplate;
	}
	public void setLuaScriptTemplate(LuaScriptTemplate luaScriptTemplate) {
		this.luaScriptTemplate = luaScriptTemplate;
	}
	
	@Around("@annotation(com.reyco.dasbx.rate.limit.annotation.TokenBucketRateLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		TokenBucketRateLimit tokenBucketRateLimit = AnnotatedElementUtils.getMergedAnnotation(
	            signature.getMethod(), TokenBucketRateLimit.class);
		if(tokenBucketRateLimit!=null) {
			String rateLimitKey = getKey(joinPoint,tokenBucketRateLimit.prefix(),tokenBucketRateLimit.value(),tokenBucketRateLimit.key());
			//限流key
			List<String> keys = Arrays.asList(rateLimitKey);
			
			//最大容量
			int capacity = tokenBucketRateLimit.capacity();
			//每周期生成的令牌数
			int rate = tokenBucketRateLimit.rate();
			//令牌生成周期
			int period = tokenBucketRateLimit.period();
			//令牌生成周期单位
			TimeUnit unit = tokenBucketRateLimit.unit();
			if(capacity <= 0 || rate <= 0 || period <= 0) {
				throw new RuntimeException("@TokenBucketRateLimit annotation configuration error,@TokenBucketRateLimit:"+tokenBucketRateLimit);
			}
			
			// 3. 将周期转换为毫秒
			long periodMillis = convertToMillis(period, unit);

			Long result = luaScriptTemplate.executeScript("TokenBucketRateLimit", keys,
					String.valueOf(capacity),
					String.valueOf(rate),
					String.valueOf(periodMillis)
			);

			if(result == 1L) {
				// 允许执行
				logger.debug("Token bucket rate limit passed - key: {}, period: {} {}, {}ms",
						rateLimitKey, period, unit, periodMillis);
				return joinPoint.proceed();
			}else {
				// 限流拒绝
				logger.warn("Token bucket rate limit blocked - key: {}", rateLimitKey);
				throw new RateLimitException("请求过于频繁，请稍后再试");
			}
		}
		return joinPoint.proceed();
    }
	
	private long convertToMillis(int period, TimeUnit unit) {
		if (period <= 0) {
			throw new IllegalArgumentException("令牌桶周期必须大于 0，当前 period=" + period);
		}
		if (unit == null) {
			throw new IllegalArgumentException("令牌桶时间单位不能为空");
		}

		long periodMillis = unit.toMillis(period);
		if (periodMillis <= 0) {
			throw new IllegalArgumentException("令牌桶周期必须至少为 1 毫秒，当前 period=" + period + ", unit=" + unit);
		}

		return periodMillis;
	}
	
}
