package com.reyco.dasbx.lock.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import com.reyco.dasbx.commons.utils.ReflectionReycoUtils;
import com.reyco.dasbx.lock.annotation.Lock;

@Aspect
@Component
public class DistributedLockAspect {
	
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
		Method targetMethod = signature.getMethod();
		Lock lock = targetMethod.getAnnotation(Lock.class);
		if(lock!=null) {
			String lockKey = getKey(joinPoint);
			String lockValue = UUID.randomUUID().toString().replace("-", "");
			try {
				distributedLock.lock(lockKey, lockValue, lock.expireTime());
				Object proceed = joinPoint.proceed();
				return proceed;
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				distributedLock.unLock(lockKey, lockValue);
			}
		}
		return joinPoint.proceed();
	}
	
	protected String getKey(ProceedingJoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method targetMethod = signature.getMethod();
		Lock lock = targetMethod.getAnnotation(Lock.class);
		String prefix = lock.prefix();
		if(!prefix.endsWith(":")) {
			prefix = prefix+":";
		}
		String key = lock.value();
		if(!key.contains("#")) {
			String lockKey = prefix+key;
			return lockKey;
		}
		//有 '#' 字符 
		Object[] parameterValues = joinPoint.getArgs();
		LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(targetMethod);
		//去掉 '#' 字符
		key = key.substring(1);
		//是否包含 '.'
		//没有'.'号
		if(!key.contains(".")) { 
			for (int i = 0; i < parameterNames.length; i++) {
				if (key.equals(parameterNames[i])) {
					String lockKey = prefix+key+":"+parameterValues[i].toString();
					return lockKey;
				}
			}
			throw new RuntimeException("value configuration error,Parameter not fount '"+key+"' value.");
		}
		//有'.' 号
		String[] keyArray = key.split("\\.");
		for (int i = 0; i < parameterNames.length; i++) {
			if ((key=keyArray[0]).equals(parameterNames[i])) {
				String lockKey = prefix+getKey(keyArray, 1, key, parameterValues[i]);
				return lockKey;
			}
		}
		throw new RuntimeException("value configuration error,Parameter not fount '"+key+"' key.");
	}
	private String getKey(String[] keyArray,int currentIndex,String key,Object value) {
		if(value==null) {
			throw new RuntimeException("Field '"+keyArray[currentIndex-1]+"' cannot be empty.");
		}
		if(keyArray.length<=currentIndex) {
			return key +":"+value.toString();
		}
		String currentKey = keyArray[currentIndex];
		if(StringUtils.isEmpty(currentKey)) {
			throw new RuntimeException("value configuration error,Parameter not fount "+currentKey+" key.");
		}
		try {
			Field field = ReflectionReycoUtils.findField(value.getClass(), currentKey);
			field.setAccessible(true);
			return getKey(keyArray, currentIndex+1, key+":"+currentKey, field.get(value));
		} catch (Exception e) {
			throw new RuntimeException("value configuration error,'"+value.getClass()+"' not found '"+currentKey+"' field.");
		}
	}
}
