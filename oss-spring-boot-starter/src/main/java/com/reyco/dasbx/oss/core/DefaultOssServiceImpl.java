package com.reyco.dasbx.oss.core;

import org.springframework.web.multipart.MultipartFile;

import com.reyco.dasbx.oss.core.exception.OssException;
import com.reyco.dasbx.oss.properties.OssProperties;

/**
 * Oss默认实现
 * @author reyco
 *
 */
public class DefaultOssServiceImpl extends AbstractOssService {
	
	private OssProperties ossProperties;
	
	public DefaultOssServiceImpl() {
	}
	public DefaultOssServiceImpl(OssProperties ossProperties) {
		super();
		this.ossProperties = ossProperties;
	}
	public void setOssProperties(OssProperties ossProperties) {
		this.ossProperties = ossProperties;
	}
	@Override
	public String upload(MultipartFile file) throws OssException {
		return upload(file, null);
	}
	@Override
	public String upload(MultipartFile multipartFile, OssParameter ossParameter) throws OssException {
		String basePath = ossProperties.getBasePath();
		String baseUploadPath = ossParameter.getUploadPath();
		String rootPath = getRootPath(basePath, baseUploadPath);
		String uploadPath = getUploadPath(basePath, baseUploadPath);
		String fileName = getFileName(multipartFile, ossParameter);
		String url = getUrl(ossProperties.getBaseUrl(), uploadPath, fileName);
		writeDisk(multipartFile, rootPath, fileName);
		return url;
	}
	@Override
	public String upload(String base64) throws OssException {
		return upload(base64, null);
	}
	@Override
	public String upload(String base64, OssParameter ossParameter) throws OssException {
		String basePath = ossProperties.getBasePath();
		String baseUploadPath = ossParameter.getUploadPath();
		String rootPath = getRootPath(basePath, baseUploadPath);
		String uploadPath = getUploadPath(basePath, baseUploadPath);
		String fileName = getFileName(base64, ossParameter);
		String url = getUrl(ossProperties.getBaseUrl(), uploadPath, fileName);
		writeDisk(base64, rootPath, fileName);
		return url;
	}
}
