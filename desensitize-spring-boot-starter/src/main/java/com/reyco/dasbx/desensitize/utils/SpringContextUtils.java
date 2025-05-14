package com.reyco.dasbx.desensitize.utils;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtils implements ApplicationContextAware {
	
	public static ApplicationContext applicationContext; 
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}
	public static String getProperty(String propertyName) {
		return applicationContext.getEnvironment().getProperty(propertyName,String.class);
	}
	public static boolean getPropertyToBoolean(String propertyName) {
		return applicationContext.getEnvironment().getProperty(propertyName,Boolean.class);
	}
	// 国际化使用
	public static String getMessage(String key) {
		return applicationContext.getMessage(key, null, Locale.getDefault());
	}


	/// 获取当前环境
	public static String getActiveProfile() {
		return applicationContext.getEnvironment().getActiveProfiles()[0];
	}

	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public static boolean isSingleton(String name) {
		return applicationContext.isSingleton(name);
	}

	public static Class<? extends Object> getType(String name) {
		return applicationContext.getType(name);
	}

}
