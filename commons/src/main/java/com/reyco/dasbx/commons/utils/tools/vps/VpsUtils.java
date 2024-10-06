package com.reyco.dasbx.commons.utils.tools.vps;

/**
 * <pre>
 * 根据文件名称获取虚拟主机----虚拟主机共1024个。
 * 根据一致性Hash散列算法，理论上文件平均分部在1024个虚拟主机上，实际可能有偏微误差。
 * 有利于后期的动态扩缩容！！
 * </pre>
 * @author reyco
 *
 */
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
	/**
	 * hash
	 * @param key
	 * @return
	 */
	private static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
