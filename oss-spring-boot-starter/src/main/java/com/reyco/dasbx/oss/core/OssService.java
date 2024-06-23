package com.reyco.dasbx.oss.core;

import org.springframework.web.multipart.MultipartFile;

import com.reyco.dasbx.oss.core.exception.OssException;

/**
 * Oss顶级接口类
 * @author reyco
 *
 */
public interface OssService {
	
	String upload(MultipartFile file) throws OssException;

	String upload(MultipartFile multipartFile, OssParameter ossParameter) throws OssException;

	String upload(String base64) throws OssException;

	String upload(String base64, OssParameter ossParameter) throws OssException;
}
