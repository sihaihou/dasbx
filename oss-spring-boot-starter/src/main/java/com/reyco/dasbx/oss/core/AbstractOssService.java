package com.reyco.dasbx.oss.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractOssService implements OssService {
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractOssService.class);
	
	public static final String LINES = "-";
	public static final String COMMA = ",";
	public static final String SPOT = ".";
	public static final String SEMICOLON = ";";
	
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddHHmmsss");
	
	protected void createDirectory(String rootPath){
		File file = new File(rootPath);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	protected String getExtension(MultipartFile multipartFile){
		String originalFilename = multipartFile.getOriginalFilename();
		return originalFilename.substring(originalFilename.lastIndexOf("."));
	}
	protected String getFilename(MultipartFile multipartFile){
		String originalFilename = multipartFile.getOriginalFilename();
		return originalFilename.substring(0,originalFilename.lastIndexOf("."));
	}
	protected String getRandomFileName(String extension) {
		String fileName = FORMAT.format(new Date()) + extension;
		return fileName;
	}
}

