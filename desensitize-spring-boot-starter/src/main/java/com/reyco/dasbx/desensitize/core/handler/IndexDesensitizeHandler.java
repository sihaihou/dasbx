package com.reyco.dasbx.desensitize.core.handler;

import org.springframework.util.StringUtils;

import com.reyco.dasbx.desensitize.core.DesensitizeType;

/**
 * 索引脱敏器
 * @author reyco
 *
 */
public class IndexDesensitizeHandler implements DesensitizeHandler{
	
	@Override
	public Boolean support(DesensitizeType type) {
		return type instanceof DesensitizeType.IndexDesensitizeType;
	}
	
	@Override
	public String handler(DesensitizeType type, String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		DesensitizeType.IndexDesensitizeType  indexType =  (DesensitizeType.IndexDesensitizeType)type;
		int len = value.length();
	    int start = Math.max(0, indexType.getStart());
	    int end = Math.min(len, indexType.getEnd());
	    //
	    if (start >= len || start >= end) {
	    	throw new RuntimeException("start or end config error");
	    }
	    
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<start;i++) {
			sb.append(value.charAt(i));
		}
		for(int i=start;i<end;i++) {
			sb.append("*");
		}
		for(int i=end;i<value.length();i++) {
			sb.append(value.charAt(i));
		}
		return sb.toString();
	}
	
}
