package com.reyco.dasbx.commons.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.reyco.dasbx.commons.utils.reflect.ReflectionReycoUtils;

/**
 * Object 转 Map
 * @author reyco
 *
 */
public class KeyValueMergeUtils {
	
	private final static int START_TIMES = 0;
	
	private final static int MAX_TIMES = 10;
	
	private final static boolean ALLOW_VALUE_IS_NULL = false;
	
	/**
	 * keys,value合并
	 * @param keys
	 * @param values
	 * @return
	 */
	public static Map<String,Object> merge(String[] keys,Object[] values){
		if(keys==null || values==null || keys.length==0 || values.length==0 || keys.length!=values.length) {
			return null;
		}
		Map<String, Object> parameters = new HashMap<String, Object>();
		for (int i = 0; i < keys.length; i++) {
			if(StringUtils.isNotBlank(keys[i]) && values[i]!=null) {
				parameters.put(keys[i], values(values[i]));
			}
		}
		return parameters;
	}
	/**
	 * object 转 key-value结构
	 * @param o
	 * @return
	 */
	public static Object values(Object o) {
		return values(o,ALLOW_VALUE_IS_NULL,MAX_TIMES,START_TIMES);
	}
	/**
	 * 合并
	 * @param o					目标对象
	 * @param allowValueIsNull	是否允许value等于空
	 * @return
	 */
	public static Object values(Object o,boolean allowValueIsNull) {
		return values(o,allowValueIsNull,MAX_TIMES,START_TIMES);
	}
	/**
	 * 合并
	 * @param o					目标对象
	 * @param maxTimes			最低递归次数，防止栈溢出
	 * @return
	 */
	public static Object values(Object o,int maxTimes) {
		return values(o,ALLOW_VALUE_IS_NULL,maxTimes,START_TIMES);
	}
	/**
	 * 合并
	 * @param o					目标对象
	 * @param allowValueIsNull	是否允许value等于空
	 * @param maxTimes			最低递归次数，防止栈溢出
	 * @return
	 */
	public static Object values(Object o,boolean allowValueIsNull,int maxTimes) {
		return values(o,allowValueIsNull,maxTimes,START_TIMES);
	}
	/**
	 * 合并
	 * @param o					目标对象
	 * @param allowValueIsNull	是否允许value等于空
	 * @param maxTimes			最低递归次数，防止栈溢出
	 * @param times				当前递归次数
	 * @return
	 */
	private static Object values(Object o,boolean allowValueIsNull,int maxTimes,int times) {
		try {
			if(times>=maxTimes || o==null) {
				return null;
			}
			if(o instanceof Number || o instanceof String|| o instanceof Boolean) {
				 String value = o.toString();
				 if(StringUtils.isBlank(value) && !allowValueIsNull) {
					 return null;
				 }
				 return value;
			}
			Field[] fields = ReflectionReycoUtils.getFields(o.getClass());
			if(fields!=null) {
				Map<String,Object> map = null;
				for(Field field : fields) {
					field.setAccessible(true);
					String key = field.getName();
					if(key.equalsIgnoreCase("serialVersionUID")) {
						continue;
					}
					Object v = values(field.get(o),allowValueIsNull,maxTimes,++times);
					if(v==null && !allowValueIsNull) {
						continue;
					}
					if(map==null) {
						map = new HashMap<String,Object>();
					}
					map.put(key, v);
				}
				if((map==null || map.size()==0) && !allowValueIsNull) {
					return null;
				}
				return map;
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
