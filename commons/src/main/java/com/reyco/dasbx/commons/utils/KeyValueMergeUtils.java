package com.reyco.dasbx.commons.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	private static Object values(Object value,boolean allowValueIsNull,int maxTimes,int depth) {
		Set<Object> path = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
		return values(value, allowValueIsNull, maxTimes, depth, path);
	}	
	
	private static Object values(Object value,boolean allowValueIsNull,int maxTimes,int depth,Set<Object> path) {
		if (depth >= maxTimes || value == null) {
			return null;
		}
		if (value instanceof Number
	            || value instanceof String
	            || value instanceof Boolean
	            || value instanceof Character
	            || value instanceof Enum<?>) {
	        String text = value.toString();
	        if (StringUtils.isBlank(text) && !allowValueIsNull) {
	            return null;
	        }
	        return text;
	    }
		
		if (value instanceof Map<?, ?>) {
	        Map<String, Object> result = new LinkedHashMap<String, Object>();
	        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
	            if (entry.getKey() == null) {
	                continue;
	            }
	            Object child = values(entry.getValue(), allowValueIsNull, maxTimes, depth + 1, path);
	            if (child != null || allowValueIsNull) {
	                result.put(entry.getKey().toString(), child);
	            }
	        }
	        return result.isEmpty() && !allowValueIsNull ? null : result;
	    }
		
		if (value instanceof Collection<?>) {
	        List<Object> result = new ArrayList<Object>();
	        for (Object item : (Collection<?>) value) {
	            Object child = values(item, allowValueIsNull, maxTimes, depth + 1, path);
	            if (child != null || allowValueIsNull) {
	                result.add(child);
	            }
	        }
	        return result.isEmpty() && !allowValueIsNull ? null : result;
	    }
		
		if (value.getClass().isArray()) {
	        List<Object> result = new ArrayList<Object>();
	        for (int i = 0; i < Array.getLength(value); i++) {
	            Object child = values(Array.get(value, i), allowValueIsNull, maxTimes, depth + 1, path);
	            if (child != null || allowValueIsNull) {
	                result.add(child);
	            }
	        }
	        return result.isEmpty() && !allowValueIsNull ? null : result;
	    }
		
		String className = value.getClass().getName();
	    if (className.startsWith("java.")
	            || className.startsWith("javax.")
	            || className.startsWith("jakarta.")
	            || className.startsWith("org.springframework.")) {
	        String text = value.toString();
	        return StringUtils.isBlank(text) && !allowValueIsNull ? null : text;
	    }

	    // 防止对象循环引用，例如 user.parent.user.parent。
	    if (!path.add(value)) {
	        return null;
	    }
	    try {
	        Field[] fields = ReflectionReycoUtils.getFields(value.getClass());
	        if (fields == null || fields.length == 0) {
	            String text = value.toString();
	            return StringUtils.isBlank(text) && !allowValueIsNull ? null : text;
	        }

	        Map<String, Object> result = new LinkedHashMap<String, Object>();
	        for (Field field : fields) {
	            if ("serialVersionUID".equalsIgnoreCase(field.getName())) {
	                continue;
	            }

	            try {
	                field.setAccessible(true);
	                Object child = values(field.get(value), allowValueIsNull,
	                        maxTimes, depth + 1, path);
	                if (child != null || allowValueIsNull) {
	                    result.put(field.getName(), child);
	                }
	            } catch (SecurityException e) {
	                // 单个字段不可访问时跳过。
	            } catch (IllegalAccessException e) {
	                // 单个字段不可访问时跳过。
	            }
	        }

	        return result.isEmpty() && !allowValueIsNull ? null : result;
	    } finally {
	        path.remove(value);
	    }
	}
	/**
	 * 合并
	 * @param o					目标对象
	 * @param allowValueIsNull	是否允许value等于空
	 * @param maxTimes			最低递归次数，防止栈溢出
	 * @param times				当前递归次数
	 * @return
	 */
	@Deprecated
	private static Object valuesOld(Object value,boolean allowValueIsNull,int maxTimes,int times) {
		try {
			if(times>=maxTimes || value==null) {
				return null;
			}
			if(value instanceof Number 
					|| value instanceof String
					|| value instanceof Boolean
					|| value instanceof Character
		            || value instanceof Enum<?>) {
				 String text = value.toString();
				 if(StringUtils.isBlank(text) && !allowValueIsNull) {
					 return null;
				 }
				 return text;
			}
			Field[] fields = ReflectionReycoUtils.getFields(value.getClass());
			if(fields!=null) {
				Map<String,Object> map = null;
				for(Field field : fields) {
					field.setAccessible(true);
					String key = field.getName();
					if(key.equalsIgnoreCase("serialVersionUID")) {
						continue;
					}
					Object text = values(field.get(value),allowValueIsNull,maxTimes,times+1);
					if(text==null && !allowValueIsNull) {
						continue;
					}
					if(map==null) {
						map = new HashMap<String,Object>();
					}
					map.put(key, text);
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
