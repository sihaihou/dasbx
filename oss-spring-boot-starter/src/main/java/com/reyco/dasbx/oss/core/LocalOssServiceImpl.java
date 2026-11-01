package com.reyco.dasbx.oss.core;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.reyco.dasbx.oss.core.exception.OssException;
import com.reyco.dasbx.oss.properties.OssProperties;

/**
 * Oss默认实现
 * @author reyco
 *
 */
public class LocalOssServiceImpl extends AbstractOssService {
	
	private OssProperties ossProperties;
	
	public LocalOssServiceImpl() {
	}
	public LocalOssServiceImpl(OssProperties ossProperties) {
		super();
		this.ossProperties = ossProperties;
	}
	public void setOssProperties(OssProperties ossProperties) {
		this.ossProperties = ossProperties;
	}
	
	@Override
	public String upload(MultipartFile multipartFile, OssParameter ossParameter) throws OssException {
		String basePath = ossProperties.getBasePath();
		String baseUploadPath = ossParameter.getUploadPath();
		
		// 1. 获取或计算文件的 Hash
	    String fileHash = getOrCalculateHash(multipartFile, ossParameter);

	    // 2.【防重/秒传核心】校验该 Hash 是否在系统中已存在
	    String existingUrl = checkHashAndGetUrl(fileHash);
	    if (StringUtils.hasLength(existingUrl)) {
	        logger.info("文件 Hash[{}] 触发秒传/去重，直接返回已有地址: {}", fileHash, existingUrl);
	        return existingUrl; // 存在直接返回已有 URL，跳过写磁盘
	    }
		
	    // 3. 不存在时，正常执行上传落盘流程
		String rootPath = getRootPath(basePath, baseUploadPath);
		String uploadPath = getUploadPath(basePath, baseUploadPath);
		String fileName = getFileName(multipartFile, ossParameter);
		String url = getUrl(ossProperties.getBaseUrl(), uploadPath, fileName);
		// 写入磁盘
		writeDisk(multipartFile, rootPath, fileName);
		
		// 4. 保存 Hash 与文件 URL 的映射关系到数据库/Redis
	    saveFileMetadata(fileHash, url, fileName, multipartFile.getSize());
	    
		return url;
	}
	
	@Override
	public String upload(String base64, OssParameter ossParameter) throws OssException {
		String basePath = ossProperties.getBasePath();
		String baseUploadPath = ossParameter.getUploadPath();
		
		// 1. 获取/计算 Base64 的 Hash
        String fileHash = getOrCalculateHash(base64, ossParameter);

        // 2. 防重/秒传校验：如果系统中已存在该 Hash 的文件，直接返回对应 URL，跳过解码和写磁盘
        String existingUrl = checkHashAndGetUrl(fileHash);
        if (StringUtils.hasLength(existingUrl)) {
            logger.info("Base64 文件 Hash[{}] 触发去重/秒传，直接返回地址: {}", fileHash, existingUrl);
            return existingUrl;
        }
        
        // 3. 不存在时，正常保存落盘
		String rootPath = getRootPath(basePath, baseUploadPath);
		String uploadPath = getUploadPath(basePath, baseUploadPath);
		String fileName = getFileName(base64, ossParameter);
		String url = getUrl(ossProperties.getBaseUrl(), uploadPath, fileName);
		// 写入磁盘
		writeDisk(base64, rootPath, fileName);
		
		// 4. 异步或同步写入元数据（以备下次去重）
        // 计算 Base64 解码后的字节大小：(base64Len * 3) / 4
        long fileSize = (base64.length() * 3L) / 4;
        saveFileMetadata(fileHash, url, fileName, fileSize);
        
		return url;
	}
}
