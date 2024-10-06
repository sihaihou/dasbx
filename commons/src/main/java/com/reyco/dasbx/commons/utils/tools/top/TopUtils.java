package com.reyco.dasbx.commons.utils.tools.top;

import java.util.List;

import com.reyco.dasbx.commons.top.Top;

/**
 * 排行榜
 * @author reyco
 *
 */
public class TopUtils {

	private final static Top<String> top = new Top<String>(10);
	/**
	 * 获取排行榜
	 * @return
	 */
	public static List<String> getTop() {
		return top.getTop();
	}
	/**
	 * 添加元素
	 * @return
	 */
	public static void add(String element) {
		add(element,1);
	}
	/**
	 * 添加元素
	 * @return
	 */
	public static void add(String element,int times) {
		top.add(element,times);
	}
}
