package com.reyco.dasbx.rate.limit.core;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
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
	
	private final static SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
	
	private final static DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
	
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
			if(capacity<=0 || rate<=0 || period<0) {
				throw new RuntimeException("@TokenBucketRateLimit annotation configuration error,@TokenBucketRateLimit:"+tokenBucketRateLimit);
			}
			
			// 3. 将周期转换为秒
	        long periodSeconds = convertToSeconds(period, unit);
	        // 4. 当前时间戳（秒）
	        long now = System.currentTimeMillis() / 1000;
	        
	        Long result = luaScriptTemplate.executeScript("TokenBucketRateLimit", keys, 
					String.valueOf(capacity),
		            String.valueOf(rate),
		            String.valueOf(periodSeconds),
		            String.valueOf(now)
		            );
	        if(result == 1L) {
	        	 // 允许执行
	        	logger.debug("Token bucket Rate limit passed - key: {}, period: {} {},{}s)", 
	        			rateLimitKey, period, unit, periodSeconds);
	            return joinPoint.proceed();
	        }else {
	        	 // 限流拒绝
	        	logger.warn("Token bucket Rate limit blocked - key: {}", rateLimitKey);
	            throw new RateLimitException("请求过于频繁，请稍后再试");
	        }
		}
		return joinPoint.proceed();
    }
	
	private long convertToSeconds(int period, TimeUnit unit) {
        switch (unit) {
            case SECONDS:
                return period;
            case MILLISECONDS:
                return period / 1000;
            case MICROSECONDS:
                return period / 1000000;
            case NANOSECONDS:
                return period / 1000000000;
            case MINUTES:
                return period * 60L;
            case HOURS:
                return period * 3600L;
            case DAYS:
                return period * 86400L;
            default:
                return period;
        }
    }
	/**
	 * 获取Key
	 * @param joinPoint
	 * @return
	 */
	@Deprecated
	protected String getKey(ProceedingJoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		TokenBucketRateLimit tokenBucketRateLimit = AnnotatedElementUtils.getMergedAnnotation(
	            signature.getMethod(), TokenBucketRateLimit.class);
		String value = getValue(joinPoint);
		if(StringUtils.isBlank(tokenBucketRateLimit.prefix()) || StringUtils.isBlank(tokenBucketRateLimit.value())) {
			throw new RuntimeException("@TokenBucketRateLimit annotation configuration error,@TokenBucketRateLimit:"+tokenBucketRateLimit);
		}
		StringJoiner joiner = new StringJoiner(":");
		joiner.add(tokenBucketRateLimit.prefix());
		joiner.add(tokenBucketRateLimit.value());
		if(StringUtils.isNotBlank(value)) {
			joiner.add(value);
		}
		return joiner.toString();
	}
	/**
	 * 获取Value
	 * @param joinPoint
	 * @return
	 */
	@Deprecated
	private String getValue(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		TokenBucketRateLimit tokenBucketRateLimit = AnnotatedElementUtils.getMergedAnnotation(
	            signature.getMethod(), TokenBucketRateLimit.class);
		String key = tokenBucketRateLimit.key();
		if(StringUtils.isBlank(key)) {
			return "";
		}
		if(!key.startsWith("#")) {
			return key;
		}
		Expression expression = spelExpressionParser.parseExpression(key);
		EvaluationContext evaluationContext = new MethodBasedEvaluationContext(joinPoint.getTarget(), signature.getMethod(), joinPoint.getArgs(), parameterNameDiscoverer);
		return expression.getValue(evaluationContext,String.class);
	}
	
}
