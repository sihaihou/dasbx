package com.reyco.dasbx.commons.utils.tools.qps;

import com.reyco.dasbx.commons.qps.Qps;

/**
 * 自定义QPS限流工具类
 * @author reyco
 *
 */
public class QPSUtils {

	private final static Qps qps = new Qps(10);
	/**
	 * 是否通过
	 * @return
	 */
	public static boolean pass() {
		return qps.pass();
	}
}
