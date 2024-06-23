package com.reyco.dasbx.constant.core;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import com.reyco.dasbx.constant.properties.ConstantProperties;

public class DefaultConstantManager implements ConstantManager{
	
	private final static String FUll_NAME_PREFIX = "reyco.dasbx.constants.";
	
	private ConstantProperties constantProperties;

	public DefaultConstantManager(@Nullable ConstantProperties constantProperties) {
		super();
		this.constantProperties = constantProperties;
	}
	public void setConstantProperties(@Nullable ConstantProperties constantProperties) {
		this.constantProperties = constantProperties;
	}
	
	@Override
	public String getProperty(String key) {
		check(key);
		if(key.startsWith(FUll_NAME_PREFIX)) {
			key = key.substring(FUll_NAME_PREFIX.length());
		}
		String oldKey = key;
		StringTokenizer stringTokenizer = new StringTokenizer(key,".");
		Object curr = constantProperties.getConstants();
		while(stringTokenizer.hasMoreTokens()) {
			key = stringTokenizer.nextToken();
			if(curr==null || curr instanceof String || !(curr instanceof Map)) {
				throw new RuntimeException("This attribute does not exist:"+oldKey);
			}
			Map<String,Object> map = (Map)curr;
			curr = map.get(key);
		}
		if(curr==null || (!(curr instanceof String) && !(curr instanceof Number))) {
			throw new RuntimeException("This attribute does not exist:"+oldKey);
		}
		return curr.toString();
	}
	
	@Override
	public void setProperty(String key,String value) {
		check(key);
		if(!StringUtils.hasLength(value)) {
			throw new RuntimeException("This attribute value not equal to empty.");
		}
		if(key.startsWith(FUll_NAME_PREFIX)) {
			key = key.substring(12);
		}
		StringTokenizer stringTokenizer = new StringTokenizer(key,".");
		Map<String,Object> root = constantProperties.getConstants();
		Map<String,Object> parent = root;
		Object curr = root;
		String currKey = "";
		while(stringTokenizer.hasMoreTokens()) {
			key = stringTokenizer.nextToken();
			if(stringTokenizer.hasMoreTokens()) {
				if(curr==null) {
					curr = new HashMap<String,Object>();
					parent.put(currKey, curr);
				}
				Map<String,Object> map = (Map)curr;
				parent = (Map<String,Object>)curr;
				curr = map.get(key);
				currKey = key;
			}else {
				if(curr==null) {
					curr = new HashMap<String,Object>();
					parent.put(currKey, curr);
				}
				Map<String,Object> map = (Map)curr;
				map.put(key, value);
			}
		}
	}
	/**
	 * 验证key是否合法
	 * @param key
	 */
	private void check(String key) {
		if(!StringUtils.hasLength(key)) {
			throw new RuntimeException("This attribute key not equal to empty.");
		}
	    if(key.startsWith(".") || key.charAt(key.length()-1)=='.') {
	    	throw new RuntimeException("Key does not allow starting or ending with a '.'");
	    }
	    char[] charArray = key.toCharArray();
	    int i = 1;
	    int count = 0;
	    while(i++<charArray.length-1) {
	    	if(charArray[i]=='.') {
	    		count++;
	    	}else {
	    		count = 0;
	    	}
	    	if(count>1) {
	    		throw new RuntimeException("Key does not allow two consecutive '.'");
	    	}
	    }
	}
}
