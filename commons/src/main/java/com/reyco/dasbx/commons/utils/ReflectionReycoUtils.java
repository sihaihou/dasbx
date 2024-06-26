package com.reyco.dasbx.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.ReflectionUtils;

public class ReflectionReycoUtils extends ReflectionUtils{
	/**
	 * 获取fields
	 * @param clazz
	 * @return
	 */
	public static Field[] getFields(Class<?> clazz) {
		Set<Field> fieldList = new HashSet<>();
		Class<?> searchType = clazz;
		while(Object.class != searchType && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			fieldList.addAll(Arrays.stream(fields).collect(Collectors.toList()));
			searchType = searchType.getSuperclass();
		}
		Field[] fields = new Field[fieldList.size()];
		fieldList.toArray(fields);
		return fields;
	}
	/**
	 * 获取Methods
	 * @param clazz
	 * @return
	 */
	public static Method[] getMethods(Class<?> clazz) {
		Set<Method> methodSet = new HashSet<Method>();
		Class<?> searchType = clazz;
		while(Object.class != searchType && searchType != null) {
			Method[] methods = searchType.getDeclaredMethods();
			methodSet.addAll(Arrays.stream(methods).collect(Collectors.toList()));
			searchType = searchType.getSuperclass();
		}
		Method[] methods = new Method[methodSet.size()];
		methodSet.toArray(methods);
		return methods;
	}
}
