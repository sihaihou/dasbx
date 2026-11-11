package com.reyco.dasbx.desensitize.resolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.reyco.dasbx.desensitize.DesensitizeType;
import com.reyco.dasbx.desensitize.annotations.Desensitize;

/**
 * 将字段上的脱敏注解解析为可执行的脱敏类型。
 */
public class DesensitizeTypeResolver {

	public DesensitizeType resolve(Desensitize desensitize) {
		if (desensitize.start() == Integer.MIN_VALUE && desensitize.end() == Integer.MAX_VALUE) {
			Class<? extends DesensitizeType> typeClass = desensitize.type();
			return instantiate(typeClass);
		}

		int start = desensitize.start() == Integer.MIN_VALUE ? 0 : desensitize.start();
		int end = desensitize.end() == Integer.MAX_VALUE ? 0 : desensitize.end();
		return new DesensitizeType.DefaultIndexDesensitizeType(start, end);
	}

	private DesensitizeType instantiate(Class<? extends DesensitizeType> typeClass) {
		try {
			Constructor<? extends DesensitizeType> constructor = typeClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (NoSuchMethodException ex) {
			throw new IllegalArgumentException("DesensitizeType requires a no-argument constructor: " + typeClass.getName(), ex);
		} catch (InstantiationException ex) {
			throw new IllegalArgumentException("Cannot instantiate DesensitizeType, it may be abstract: " + typeClass.getName(), ex);
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException("Cannot access DesensitizeType constructor: " + typeClass.getName(), ex);
		} catch (InvocationTargetException ex) {
			throw new IllegalStateException("DesensitizeType constructor threw an exception: " + typeClass.getName(),ex.getCause());
		}
	}
}
