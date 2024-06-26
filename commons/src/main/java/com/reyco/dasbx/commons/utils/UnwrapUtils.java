package com.reyco.dasbx.commons.utils;

import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * 字符串解包装:
 * 	<pre>
 * 	例： 字符串   
 * 			v:  aaaa{bbbb}aaaa
 * 		集合Map
 *        map:	key=bbbb value:cccc
 * 		  结果：   
 * 			v:  aaaaccccaaaa
 *  <pre>
 * @author reyco
 *
 */
public class UnwrapUtils {
	
	public static final String PREFIX = "{";
	
	public static final String SUFFIX = "}";

	/** 
	 * 
	 * @param value
	 * @return
	 */
	public static String unwrapValue(String v,Map<String,Object> map) {
		String r,sv = "";
		if(hasWrap(v)) {
			sv = v;
			int si,ei=0;
			if ((si=sv.lastIndexOf(PREFIX)) != -1) {
				if ((ei=findWrapEndIndex(sv, si)) != -1) {
					String svp = sv.substring(0,si);
					String vp = sv.substring(si + PREFIX.length(), ei);
					String mvp = map.get(vp).toString();
					String evp = sv.substring(ei+1);
					r = svp + mvp + evp;
					return unwrapValue(r,map);
				}
			}
		}
		return v;
	}
	/**
	 * 获取索引结束索引
	 * @param b
	 * @param si
	 * @return
	 */
	private static int findWrapEndIndex(CharSequence b, int si) {
		int i = si + PREFIX.length();
		int withinNestedPlaceholder = 0;
		while (i < b.length()) {
			if (StringUtils.substringMatch(b, i, SUFFIX)) {
				if (withinNestedPlaceholder > 0) {
					withinNestedPlaceholder--;
					i = i + SUFFIX.length();
				} else {
					return i;
				}
			} else if (StringUtils.substringMatch(b, i, PREFIX)) {
				withinNestedPlaceholder++;
				i = i + PREFIX.length();
			} else {
				i++;
			}
		}
		return -1;
	}
	/**
	 * 验证是否包含PREFIX、SUFFIX
	 * @param property
	 * @return
	 */
	private static boolean hasWrap(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        final String tv = path.trim();
        int pi,ps;
        if((pi=tv.lastIndexOf(PREFIX))!=-1 && (ps=tv.indexOf(SUFFIX))!=-1 && pi<ps) {
        	return true;
        }
        return false;
    }
	
	
}
