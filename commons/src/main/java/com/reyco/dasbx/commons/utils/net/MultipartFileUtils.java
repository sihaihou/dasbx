package com.reyco.dasbx.commons.utils.net;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public class MultipartFileUtils {
	/**
	 * 获取文件名称(不包含后缀)
	 * @param file
	 * @return
	 */
	public static String getFilename(MultipartFile file) {
		String fullFilename = getFullFilename(file);
		return fullFilename.substring(0,fullFilename.lastIndexOf("."));
	}
	/**
	 * 获取文件名称(包含后缀)
	 * @param file
	 * @return
	 */
	public static String getFullFilename(MultipartFile file) {
		return file.getOriginalFilename();
	}
	/**
	 * 获取文件扩展名(.mp4、.jpg)
	 * @param file
	 * @return
	 */
	public static String getExtension(MultipartFile file){
		String fullFilename = getFullFilename(file);
		return fullFilename.substring(fullFilename.lastIndexOf("."));
	}
	/**
	 * 创建目录
	 * @param rootPath
	 */
	public static void mkdirs(String rootPath){
		File file = new File(rootPath);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
}
