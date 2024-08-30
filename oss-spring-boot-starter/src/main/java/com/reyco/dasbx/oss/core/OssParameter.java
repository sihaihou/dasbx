package com.reyco.dasbx.oss.core;

/**
 * Oss参数
 * @author reyco
 *
 */
public interface OssParameter {
	
	String getUploadPath();
	
	default String getFilename() {
		return null;
	}
	
}
