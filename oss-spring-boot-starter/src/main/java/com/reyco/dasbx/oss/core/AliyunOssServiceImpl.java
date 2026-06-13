package com.reyco.dasbx.oss.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.reyco.dasbx.oss.core.exception.OssException;
import com.reyco.dasbx.oss.properties.OssProperties;

/**
 * 阿里云 OSS 存储策略实现
 * @author reyco
 */
public class AliyunOssServiceImpl extends AbstractOssService {

	private final OssProperties ossProperties;
	
	private final OSS ossClient;

	public AliyunOssServiceImpl(OssProperties ossProperties) {
		this.ossProperties = ossProperties;
		
		// 从我们之前设计好的统一属性类中提取阿里云专属配置
		OssProperties.Aliyun aliyunConfig = ossProperties.getAliyun();
		
		if (aliyunConfig == null) {
			throw new IllegalArgumentException("阿里云 OSS 配置项 [reyco.dasbx.oss.aliyun] 不能为空！");
		}
		
		// 初始化阿里云官方原生高级客户端
		this.ossClient = new OSSClientBuilder().build(
				aliyunConfig.getEndpoint(),
				aliyunConfig.getAccessKeyId(),
				aliyunConfig.getAccessKeySecret()
		);
	}

	@Override
	public String upload(MultipartFile multipartFile, OssParameter ossParameter) throws OssException {
		ossParameter = (ossParameter == null) ? DEFAULT_PARAMETER : ossParameter;
		OssProperties.Aliyun config = ossProperties.getAliyun();

		// 1. 利用父类极其优秀的路径计算逻辑，算出相对云盘路径（例如：order/202605/）
		String uploadPath = getUploadPath(ossProperties.getBasePath(), ossParameter.getUploadPath());
		
		// 2. 利用父类防覆盖逻辑算出唯一文件名（例如：avatar-20260528231500888.png）
		String fileName = getFileName(multipartFile, ossParameter);
		
		// 3. 拼接出阿里云上唯一的绝对 Object 路径
		String objectName = uploadPath + fileName;

		// 4. 调用阿里云 SDK 将流推向云端
		try (InputStream is = multipartFile.getInputStream()) {
			// 设置选填的元数据（如设置内容类型，防止图片在浏览器打开时变成强制下载）
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(multipartFile.getContentType());
			
			ossClient.putObject(config.getBucketName(), objectName, is, metadata);
			
			// 5. 返回由用户配置的 baseUrl 拼接出的绝对公网访问链接
			return getUrl(ossProperties.getBaseUrl(), uploadPath, fileName);
		} catch (Exception e) {
			logger.error("阿里云 OSS 上传文件失败, objectName: {}, 错误信息: {}", objectName, e.getMessage());
			throw new OssException("阿里云 OSS 上传失败: " + multipartFile.getOriginalFilename());
		}
	}

	@Override
	public String upload(String base64, OssParameter ossParameter) throws OssException {
		ossParameter = (ossParameter == null) ? DEFAULT_PARAMETER : ossParameter;
		OssProperties.Aliyun config = ossProperties.getAliyun();

		String uploadPath = getUploadPath(ossProperties.getBasePath(), ossParameter.getUploadPath());
		String fileName = getFileName(base64, ossParameter);
		String objectName = uploadPath + fileName;

		// 解析 base64
		String base64String = base64.split(COMMA)[1];
		byte[] bytes = Base64.getDecoder().decode(base64String);

		// 将字节数组转成内存流推给阿里云
		try (InputStream is = new ByteArrayInputStream(bytes)) {
			ObjectMetadata metadata = new ObjectMetadata();
			// 动态猜测 base64 的 mime 类型（如 image/png）
			String mimeType = base64.split(COMMA)[0].split(SEMICOLON)[0].replace("data:", "");
			metadata.setContentType(mimeType);

			ossClient.putObject(config.getBucketName(), objectName, is, metadata);
			
			return getUrl(ossProperties.getBaseUrl(), uploadPath, fileName);
		} catch (Exception e) {
			logger.error("阿里云 OSS 上传 Base64 失败, objectName: {}, 错误信息: {}", objectName, e.getMessage());
			throw new OssException("阿里云 OSS 上传 Base64 失败");
		}
	}

	/**
	 * 组件被销毁时（应用关闭），优雅释放阿里云的长连接客户端，防止线程挂起和内存泄露
	 */
	public void shutdown() {
		if (this.ossClient != null) {
			this.ossClient.shutdown();
		}
	}
}
