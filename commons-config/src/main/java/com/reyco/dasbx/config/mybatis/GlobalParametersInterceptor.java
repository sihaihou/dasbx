package com.reyco.dasbx.config.mybatis;

import java.lang.reflect.Field;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.ReflectionReycoUtils;
import com.reyco.dasbx.config.utils.TokenUtils;

@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class GlobalParametersInterceptor implements Interceptor {
	private final static String MODIFIEDBY = "modifiedBy";
	private final static String GMTMODIFIED = "gmtModified";
	private final static String CREATEBY = "createBy";
	private final static String GMTCREATE = "gmtCreate";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object params = args[1];
		updateField(params, params.getClass(), mappedStatement.getSqlCommandType());
		return invocation.proceed();
	}
	/**
	 * 设置属性
	 * 
	 * @param params
	 * @param clazz
	 * @param parameter
	 * @param sqlCommandType
	 * @throws Exception
	 */
	private void updateField(Object target, Class<?> targetClass, SqlCommandType sqlCommandType) throws Exception{
		Long id = TokenUtils.getToken().getId();
		Long currentTime = Dasbx.getCurrentTime();
		wrapTarget(target, targetClass, MODIFIEDBY, id);
		wrapTarget(target, targetClass, GMTMODIFIED, currentTime);
		if (SqlCommandType.INSERT.equals(sqlCommandType)) {
			wrapTarget(target, targetClass, CREATEBY, id);
			wrapTarget(target, targetClass, GMTCREATE, currentTime);
			wrapTarget(target, targetClass, MODIFIEDBY, id);
			wrapTarget(target, targetClass, GMTMODIFIED, currentTime);
		}
	}
	/**
	 * 设置目标属性
	 * @param target
	 * @param targetClass
	 * @param fieldName
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void wrapTarget(Object target, Class<?> targetClass, String fieldName, Object value)
			throws IllegalArgumentException, IllegalAccessException {
		Field gmtModifiedField = ReflectionReycoUtils.findField(targetClass, fieldName);
		if (gmtModifiedField != null) {
			gmtModifiedField.setAccessible(true);
			if (gmtModifiedField.get(target) == null) {
				gmtModifiedField.set(target, value);
			}
		}
	}
}
