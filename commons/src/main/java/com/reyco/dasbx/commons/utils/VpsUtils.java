package com.reyco.dasbx.commons.utils;

public class VpsUtils {
	/**
	 * 获取虚拟主机
	 * @param filename
	 * @return
	 */
	public static int getVps(String filename) {
		int hash = hash(filename);
		int vps = Math.abs(hash)%1024;
		return vps;
	}
	private static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
