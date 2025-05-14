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
		DesensitizeType.IndexDesensitizeType  indexDesensitizeType =  (DesensitizeType.IndexDesensitizeType)type;
		int start = indexDesensitizeType.getStart() < 0 ? 0 : indexDesensitizeType.getStart() > value.length() ? value.length() : indexDesensitizeType.getStart();
		int end = indexDesensitizeType.getEnd() > value.length() ? value.length() : indexDesensitizeType.getEnd();
		if(start <= end) {
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
		throw new RuntimeException("start or end error");
	}
	
}
