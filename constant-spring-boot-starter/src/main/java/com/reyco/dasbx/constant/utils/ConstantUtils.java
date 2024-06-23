package com.reyco.dasbx.constant.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.constant.core.ConstantManager;

/**
 * 获取常量信息
 * @author reyco
 *
 */
public class ConstantUtils {
	
	private static Logger logger = LoggerFactory.getLogger(ConstantUtils.class);
	
	private ConstantManager constantManager;
	public ConstantUtils(ConstantManager constantManager) {
		super();
		this.constantManager = constantManager;
	}
	public void setConstantManager(ConstantManager constantManager) {
		this.constantManager = constantManager;
	}
	/**
	 * 根据key获取常量
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return constantManager.getProperty(key);
	}
	/**
	 * 根据key获取常量，如果为null or "",返回 defaultValue
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String key,String defaultValue) {
		try {
			return constantManager.getProperty(key);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	/**
	 * 设置常量属性
	 * @param key
	 * @param value
	 * @return
	 */
	public String setProperty(String key,String value) {
		String oldValue = constantManager.getProperty(key);
		constantManager.setProperty(key, oldValue);
		return oldValue;
	}
}
