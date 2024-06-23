package com.reyco.dasbx.oss.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;

import org.springframework.util.StringUtils;
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
		InputStream is = null;
		OutputStream os = null;
		try {
			String basePath = ossProperties.getBasePath();
			if (!basePath.endsWith("/")) {
				basePath = basePath + "/";
			}
			String rootPath = basePath;
			String uploadPath = "";
			if (ossParameter != null) {
				uploadPath = ossParameter.getUploadPath();
				if (StringUtils.hasLength(uploadPath)) {
					if (!uploadPath.endsWith("/")) {
						uploadPath = uploadPath + "/";
					}
					if (uploadPath.contains(rootPath)) {
						rootPath = uploadPath;
					} else {
						rootPath = (rootPath + uploadPath).replace("//", "/");
					}
					uploadPath = rootPath.replace(basePath, "");
				}
			}
			createDirectory(rootPath);
			String extension = getExtension(multipartFile);
			String filename = getFilename(multipartFile);
			String fileName = filename + LINES + FORMAT.format(new Date()) + extension;
			is = multipartFile.getInputStream();
			os = new BufferedOutputStream(new FileOutputStream(new File(rootPath, fileName)));
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.flush();
			String resultPath = "";
			String baseUrl = ossProperties.getBaseUrl();
			if (!baseUrl.endsWith("/")) {
				baseUrl = baseUrl + "/";
			}
			resultPath = baseUrl + uploadPath + fileName;
			return resultPath;
		} catch (IOException e) {
			logger.error("Upload exception,filename：" + multipartFile.getOriginalFilename()+",e:"+e.getMessage());
			throw new OssException("Upload exception,filename：" + multipartFile.getOriginalFilename());
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String upload(String base64) throws OssException {
		return upload(base64, null);
	}

	@Override
	public String upload(String base64, OssParameter ossParameter) throws OssException {
		OutputStream os = null;
		try {
			// base64编码
			String base64String = base64.split(COMMA)[1];
			// data:image/png;base64,
			String base64Data = base64.split(COMMA)[0];
			// 文件扩展名  .png
			String extension = SPOT + base64Data.substring(base64Data.indexOf("/") + 1, base64Data.indexOf(SEMICOLON));
			//byte[] bytes = Base64Utils.decode(base64String);
			byte[] bytes = Base64.getDecoder().decode(base64String);
			// 处理数据
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
			String basePath = ossProperties.getBasePath();
			if (!basePath.endsWith("/")) {
				basePath = basePath + "/";
			}
			String rootPath = basePath;
			String uploadPath = "";
			if (ossParameter != null) {
				uploadPath = ossParameter.getUploadPath();
				if (StringUtils.hasLength(uploadPath)) {
					if (!uploadPath.endsWith("/")) {
						uploadPath = uploadPath + "/";
					}
					if (uploadPath.contains(rootPath)) {
						rootPath = uploadPath;
					} else {
						rootPath = (rootPath + uploadPath).replace("//", "/");
					}
					uploadPath = rootPath.replace(basePath, "");
				}
			}
			createDirectory(rootPath);
			String fileName = getRandomFileName(extension);
			// 生成jpeg图片
			os = new BufferedOutputStream(new FileOutputStream(new File(rootPath, fileName)));
			os.write(bytes);
			os.flush();
			String resultPath = "";
			String baseUrl = ossProperties.getBaseUrl();
			if (!baseUrl.endsWith("/")) {
				baseUrl = baseUrl + "/";
			}
			resultPath = baseUrl + uploadPath + fileName;
			return resultPath;
		} catch (IOException e) {
			logger.error("Upload exception,e:"+e.getMessage());
			throw new OssException("Upload exception,e:"+e.getMessage());
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
