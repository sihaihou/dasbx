package com.reyco.dasbx.oss.core;

import org.springframework.util.StringUtils;

/**
 * Oss参数
 * @author reyco
 *
 */
public interface OssParameter {
	/**
	 * 获取上传目录
	 * @return
	 */
	String getUploadPath();
	/**
	 * 获取文件名(含扩展名:后缀)
	 * @return
	 */
	default String getFileName(String filename,String extension) {
		String newFilename = getFilename(filename);
		if(StringUtils.hasLength(newFilename)) {
			filename = newFilename;
		}
		return filename+extension;
	}
	/**
	 * 获取文件名(不含扩展名:后缀)
	 * @param file
	 * @return
	 */
	default String getFilename(String fileName) {
		return fileName;
	}
	
}
