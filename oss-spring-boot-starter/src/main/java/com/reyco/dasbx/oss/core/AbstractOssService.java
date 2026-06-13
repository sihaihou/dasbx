package com.reyco.dasbx.oss.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.reyco.dasbx.oss.core.exception.OssException;
import com.reyco.dasbx.oss.utils.MultipartFileUtils;

public abstract class AbstractOssService implements OssService {
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractOssService.class);
	
	public static final String LINES = "-";
	public static final String COMMA = ",";
	public static final String SPOT = ".";
	public static final String SEMICOLON = ";";
	
	public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	
	protected static final OssParameter DEFAULT_PARAMETER = () -> "";
	
	@Override
	public String upload(MultipartFile file) throws OssException {
		return upload(file, DEFAULT_PARAMETER);
	}

	@Override
	public String upload(String base64) throws OssException {
		return upload(base64, DEFAULT_PARAMETER);
	}
	/**
	 * 获取rootPath
	 * @param basePath
	 * @param uploadPath
	 * @return
	 * @throws OssException 
	 */
	protected String getRootPath(String basePath,String uploadPath) throws OssException {
		if(!StringUtils.hasLength(basePath)) {
			throw new OssException("BasePath cannot be empty!");
		}
		if (!basePath.endsWith("/")) {
			basePath = basePath + "/";
		}
		String rootPath = basePath;
		if (StringUtils.hasLength(uploadPath)) {
			if (!uploadPath.endsWith("/")) {
				uploadPath = uploadPath + "/";
			}
			if (uploadPath.contains(rootPath)) {
				rootPath = uploadPath;
			} else {
				rootPath = (rootPath + uploadPath).replace("//", "/");
			}
		}
		MultipartFileUtils.mkdirs(rootPath);
		return rootPath;
	}
	/**
	 * 获取uploadPath
	 * @param basePath
	 * @param uploadPath
	 * @return
	 * @throws OssException 
	 */
	protected String getUploadPath(String basePath,String uploadPath) throws OssException {
		String rootPath = getRootPath(basePath, uploadPath);
		uploadPath = rootPath.replace(basePath, "");
		if(uploadPath.startsWith("/")) {
			uploadPath = uploadPath.substring(1);
		}
		if(!uploadPath.endsWith("/")) {
			uploadPath = uploadPath+"/";
		}
		return uploadPath;
	}
	
	/**
	 * 获取文件名称(含扩展名)
	 * @param multipartFile
	 * @param ossParameter
	 * @return
	 */
	protected String getFileName(MultipartFile multipartFile,OssParameter ossParameter) {
		//文件名
		String filename = MultipartFileUtils.getFilename(multipartFile);
		//文件扩展名  .png
		String extension = MultipartFileUtils.getExtension(multipartFile);
		return getFileName(filename, extension, ossParameter);
	}
	/**
	 * 获取文件名称(含扩展名)
	 * @param base64
	 * @param ossParameter
	 * @return
	 */
	protected String getFileName(String base64,OssParameter ossParameter) {
		//文件名
		String filename = getRandomFilename();
		// data:image/png;base64,
		String base64Data = base64.split(COMMA)[0];
		// 文件扩展名  .png
		String extension = SPOT + base64Data.substring(base64Data.indexOf("/") + 1, base64Data.indexOf(SEMICOLON));
		return getFileName(filename, extension, ossParameter);
	}
	/**
	 * 获取文件名称(含扩展名)
	 * @param base64
	 * @param ossParameter
	 * @return
	 */
	private String getFileName(String filename,String extension,OssParameter ossParameter) {
		// 1. 防御：如果 parameter 为空，直接用空对象兜底，彻底杜绝 NPE
	    ossParameter = (ossParameter == null) ? DEFAULT_PARAMETER : ossParameter;
	    
	    String fileName = ossParameter.getFileName(filename, extension);
	    
	    // 2. 关键判断：只有当用户真正“定制了新名字”时（即返回值不等于原文件名+后缀），才直接返回
	    if (StringUtils.hasLength(fileName) && !fileName.equals(filename + extension)) {
	        return fileName;
	    }
	    
	    // 3. 如果用户只是想改纯文件名部分
	    String newFilename = ossParameter.getFilename(filename);
	    if (StringUtils.hasLength(newFilename)) {
	        filename = newFilename;
	    }
	    
	    // 4. 线程安全的时间戳拼接
	    return filename + LINES + LocalDateTime.now().format(FORMAT) + extension;
	}
	/**
	 * 文件访问地址
	 * @param baseUrl
	 * @param uploadPath
	 * @param fileName
	 * @return
	 */
	protected String getUrl(String baseUrl,String uploadPath,String fileName) {
		if (!baseUrl.endsWith("/")) {
			baseUrl = baseUrl + "/";
		}
		String url = baseUrl + uploadPath + fileName;
		return url;
	}
	/**
	 * 写入磁盘
	 * @param multipartFile
	 * @param rootPath
	 * @param fileName
	 * @throws OssException
	 */
	protected void writeDisk(MultipartFile multipartFile,String rootPath,String fileName) throws OssException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = multipartFile.getInputStream();
			os = new BufferedOutputStream(new FileOutputStream(new File(rootPath, fileName)));
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.flush();
		} catch (IOException e) {
			logger.error("Upload exception,filename：" + multipartFile.getOriginalFilename()+",e:"+e.getMessage());
			throw new OssException("Upload exception,filename：" + multipartFile.getOriginalFilename());
		}finally {
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
	/**
	 * 写入磁盘
	 * @param base64	
	 * @param rootPath
	 * @param fileName
	 * @throws OssException
	 */
	protected void writeDisk(String base64,String rootPath,String fileName) throws OssException {
		// base64编码
		String base64String = base64.split(COMMA)[1];
		byte[] bytes = Base64.getDecoder().decode(base64String);
		// 处理数据
		/*for (int i = 0; i < bytes.length; ++i) {
			if (bytes[i] < 0) {
				bytes[i] += 256;
			}
		}*/
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(new File(rootPath, fileName)));
			os.write(bytes);
			os.flush();
		} catch (IOException e) {
			logger.error("Upload exception,e:"+e.getMessage());
			throw new OssException("Upload exception,e:"+e.getMessage());
		}finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	protected String getRandomFilename() {
		return LocalDateTime.now().format(FORMAT);
	}
}

