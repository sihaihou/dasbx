package com.reyco.dasbx.constant.core;

/**
 * 常量管理接口类
 * @author reyco
 *
 */
public interface ConstantManager {
	/**
	 * 获取属性
	 * @param key
	 * @return
	 */
	String getProperty(String key);
	/**
	 * 设置属性
	 * @param key
	 * @param value
	 */
	void setProperty(String key,String value);
}
