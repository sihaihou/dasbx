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
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.model.vo.SysAccountToken;

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
		updateField(params, params.getClass(), mappedStatement.getSqlCommandType(), 0, 0);
		return invocation.proceed();
	}

	/**
	 * 设置属性
	 * 
	 * @param params
	 * @param clazz
	 * @param parameter
	 * @param sqlCommandType
	 * @throws AuthenticationException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private void updateField(Object target, Class<?> targetClass, SqlCommandType sqlCommandType, int updateTimes,
			int insertTimes) throws AuthenticationException, IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		SysAccountToken token = null;
		if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
			Field modifiedByField = getField(targetClass, MODIFIEDBY);
			if (modifiedByField != null) {
				updateTimes++;
				modifiedByField.setAccessible(true);
				if (modifiedByField.get(target) == null) {
					token = TokenUtils.getToken();
					modifiedByField.set(target, token.getId());
				}
			}
			Field gmtModifiedField = getField(targetClass, GMTMODIFIED);
			if (gmtModifiedField != null) {
				updateTimes++;
				gmtModifiedField.setAccessible(true);
				if (gmtModifiedField.get(target) == null) {
					gmtModifiedField.set(target, Dasbx.getCurrentTime());
				}
			}
		} else if (SqlCommandType.INSERT.equals(sqlCommandType)) {
			// set创建属性
			if (insertTimes == 0) {
				Field createByField = getField(targetClass, CREATEBY);
				if (createByField != null) {
					insertTimes++;
					createByField.setAccessible(true);
					if (createByField.get(target) == null) {
						if (token == null) {
							token = TokenUtils.getToken();
						}
						createByField.set(target, token.getId());
					}
				}
				Field gmtCreateField = getField(targetClass, GMTCREATE);
				if (gmtCreateField != null) {
					insertTimes++;
					gmtCreateField.setAccessible(true);
					if (gmtCreateField.get(target) == null) {
						gmtCreateField.set(target, Dasbx.getCurrentTime());
					}
				}
			}
			if (updateTimes == 0) {
				// set更新属性
				Field modifiedByField = getField(targetClass, MODIFIEDBY);
				if (modifiedByField != null) {
					updateTimes++;
					modifiedByField.setAccessible(true);
					if (modifiedByField.get(target) == null) {
						if (token == null) {
							token = TokenUtils.getToken();
						}
						modifiedByField.set(target, token.getId());
					}
				}
				Field gmtModifiedField = getField(targetClass, GMTMODIFIED);
				if (gmtModifiedField != null) {
					updateTimes++;
					gmtModifiedField.setAccessible(true);
					if (gmtModifiedField.get(target) == null) {
						gmtModifiedField.set(target, Dasbx.getCurrentTime());
					}
				}
			}
		}
		if (updateTimes == 0 || insertTimes == 0) {
			Class<?> superclass = targetClass.getSuperclass();
			if (superclass != null) {
				if (SqlCommandType.INSERT.equals(sqlCommandType) && (updateTimes == 0 || insertTimes == 0)) {
					updateField(target, superclass, sqlCommandType, updateTimes, insertTimes);
				}
				if (SqlCommandType.UPDATE.equals(sqlCommandType) && updateTimes == 0) {
					updateField(target, superclass, sqlCommandType, updateTimes, insertTimes);
				}
			}
		}
	}

	private static Field getField(Class<?> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			return field;
		} catch (Exception e) {

		}
		return null;
	}
}
