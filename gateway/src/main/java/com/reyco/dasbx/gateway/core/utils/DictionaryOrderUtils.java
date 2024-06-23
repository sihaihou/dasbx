package com.reyco.dasbx.gateway.core.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;

/**
 * 字典序工具类
 * @author reyco
 *
 */
public class DictionaryOrderUtils {
	
	public static void main(String[] args) {
		Map<String,Object> paramterMap = new HashMap<>();
		paramterMap.put("d", "4");
		paramterMap.put("b", "2");
		paramterMap.put("c", "3");
		paramterMap.put("a", "1");
		String dictionaryOrder = lowestDictionaryOrderString(paramterMap);
		System.out.println(dictionaryOrder);
	}
	/**
	 * 最小字典序
	 * @param paramterMap
	 * @return
	 */
	public static String lowestDictionaryOrderString(Map<String,Object> paramterMap) {
		Set<String> keys = paramterMap.keySet();
		if(keys==null || keys.size()==0) {
			return "";
		}
		String[] strs = keys.toArray(new String[keys.size()]);
		Arrays.sort(strs,new StringComparator());
		StringJoiner stringJoiner = new StringJoiner("&","?","");
		for (int i = 0; i < strs.length; i++) {
			String key = strs[i];
			if(paramterMap.get(key)!=null && StringUtils.isNotEmpty(paramterMap.get(key).toString())) {
				stringJoiner.add(key+"="+paramterMap.get(key));
			}
		}
		return stringJoiner.toString();
	}
	/**
	 * 字符串字典序排序比较强
	 * @author reyco
	 *
	 */
	public static class StringComparator implements Comparator<String>{
		@Override
		public int compare(String o1, String o2) {
			return (o1+o2).compareTo(o2+o1);
		}
	}
	
}

