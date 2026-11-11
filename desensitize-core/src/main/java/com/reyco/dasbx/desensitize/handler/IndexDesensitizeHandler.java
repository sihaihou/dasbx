package com.reyco.dasbx.desensitize.handler;

import com.reyco.dasbx.desensitize.DesensitizeType;

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
		if (value == null || value.isEmpty()) {
		    return null;
		}
		DesensitizeType.IndexDesensitizeType  indexType =  (DesensitizeType.IndexDesensitizeType)type;
		int len = value.length();
		int prefixLength = Math.min(len, Math.max(0, indexType.getStart()));
		int suffixLength = Math.min(len, Math.max(0, indexType.getEnd()));
		int suffixStart = Math.max(prefixLength, len - suffixLength);
		
		StringBuilder sb = new StringBuilder();
		if (prefixLength >= suffixStart) {
			for (int i = 0; i < len; i++) {
				sb.append("*");
			}
			return sb.toString();
		}
		for(int i=0;i<prefixLength;i++) {
			sb.append(value.charAt(i));
		}
		for(int i=prefixLength;i<suffixStart;i++) {
			sb.append("*");
		}
		for(int i=suffixStart;i<value.length();i++) {
			sb.append(value.charAt(i));
		}
		return sb.toString();
	}
	
}
