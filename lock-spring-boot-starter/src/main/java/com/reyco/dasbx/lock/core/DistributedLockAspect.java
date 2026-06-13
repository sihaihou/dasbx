package com.reyco.dasbx.lock.core;

import java.util.StringJoiner;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.reyco.dasbx.lock.annotation.Lock;

@Aspect
public class DistributedLockAspect {
	
	private static SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
	
	private static DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
	
	private DistributedLock distributedLock;

	public DistributedLock getDistributedLock() {
		return distributedLock;
	}

	public void setDistributedLock(DistributedLock distributedLock) {
		this.distributedLock = distributedLock;
	}
	@Pointcut("@annotation(com.reyco.dasbx.lock.annotation.Lock)")
	public void lockPointCut() {
	}

	@Around("lockPointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Lock lock = AnnotatedElementUtils.getMergedAnnotation(signature.getMethod(), Lock.class);
		if(lock!=null) {
			String lockKey = getKey(joinPoint);
			String lockValue = UUID.randomUUID().toString().replace("-", "");
			try {
				distributedLock.lock(lockKey, lockValue, lock.expireTime());
				return joinPoint.proceed();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				distributedLock.unLock(lockKey, lockValue);
			}
		}
		return joinPoint.proceed();
	}
	/**
	 * 获取Key
	 * @param joinPoint
	 * @return
	 */
	protected String getKey(ProceedingJoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Lock lock = AnnotatedElementUtils.getMergedAnnotation(signature.getMethod(), Lock.class);
		String value = getValue(joinPoint);
		if(StringUtils.isBlank(lock.prefix()) && StringUtils.isBlank(lock.value()) && StringUtils.isBlank(value)) {
			throw new RuntimeException("@Lock annotation configuration error,@Lock:"+lock);
		}
		StringJoiner joiner = new StringJoiner(":");
		joiner.add(lock.prefix());
		joiner.add(lock.value());
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
	private String getValue(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Lock lock = AnnotatedElementUtils.getMergedAnnotation(signature.getMethod(), Lock.class);
		String key = lock.key();
		if(StringUtils.isBlank(key)) {
			return "";
		}
		if(!key.startsWith("#")) {
			return key;
		}
		Expression expression = spelExpressionParser.parseExpression(key);
		EvaluationContext evaluationContext = new MethodBasedEvaluationContext(joinPoint.getTarget(), signature.getMethod(), joinPoint.getArgs(),parameterNameDiscoverer);
		return expression.getValue(evaluationContext,String.class);
	}
	
}