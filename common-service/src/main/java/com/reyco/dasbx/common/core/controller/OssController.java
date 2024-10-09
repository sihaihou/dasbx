package com.reyco.dasbx.common.core.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.commons.utils.tools.vps.VpsUtils;
import com.reyco.dasbx.commons.utils.video.VideoUtils;
import com.reyco.dasbx.config.DasbxConfig;
import com.reyco.dasbx.id.core.IdGenerator;
import com.reyco.dasbx.oss.core.OssParameter;
import com.reyco.dasbx.oss.core.OssService;
import com.reyco.dasbx.oss.core.exception.OssException;

@RestController
@RequestMapping("oss")
public class OssController {
	
	@Autowired
	private OssService ossService;
	@Autowired
	private DasbxConfig dasbxConfig;
	@Autowired
	private IdGenerator<Long> idGenerator;
	
	@PostMapping("upload")
	public Object upload(@RequestParam("file") MultipartFile file) throws OssException {
		Long id = idGenerator.getGeneratorId();
		final String filename = id.toString();
		String url = ossService.upload(file, new OssParameter() {
			@Override
			public String getUploadPath() {
				Integer vps = VpsUtils.getVps(filename);
				return dasbxConfig.getBasePath()+"/"+vps;
			}
		});
		return R.success(url);
	}
	@PostMapping("uploadImage")
	public Object uploadImage(@RequestParam("file") MultipartFile file) throws OssException {
		Long id = idGenerator.getGeneratorId();
		final String filename = id.toString();
		String url = ossService.upload(file, new OssParameter() {
			@Override
			public String getUploadPath() {
				Integer vps = VpsUtils.getVps(filename);
				return dasbxConfig.getBaseImagePath()+"/"+vps;
			}
		});
		return R.success(url);
	}
	@PostMapping("uploadVideo")
	public Object uploadVideo(@RequestParam("file") MultipartFile file) throws OssException, IOException {
		Long id = idGenerator.getGeneratorId();
		final String filename = id.toString();
		String url = ossService.upload(file, new OssParameter() {
			@Override
			public String getUploadPath() {
				Integer vps = VpsUtils.getVps(filename);
				return dasbxConfig.getBaseVideoPath()+"/"+vps;
			}
		});
		BufferedImage imagePortrait = VideoUtils.getCoverImage(file.getInputStream(),315,420);
		String base64Portrait = VideoUtils.bufferedImageToBase64(imagePortrait);
		String imagePortraitUrl = ossService.upload(base64Portrait, new OssParameter() {
			@Override
			public String getUploadPath() {
				Integer vps = VpsUtils.getVps(filename);
				return dasbxConfig.getBaseImagePath()+"/"+vps;
			}
			@Override
			public String getFilename(String f) {
				return filename+"-c";
			}
		});
		BufferedImage imageLandscape = VideoUtils.getCoverImage(file.getInputStream(),450,250);
		String base64Landscape = VideoUtils.bufferedImageToBase64(imageLandscape);
		String imageLandscapeUrl = ossService.upload(base64Landscape, new OssParameter() {
			@Override
			public String getUploadPath() {
				Integer vps = VpsUtils.getVps(filename);
				return dasbxConfig.getBaseImagePath()+"/"+vps;
			}
			@Override
			public String getFilename(String f) {
				return filename+"-r";
			}
		});
		Map<String,String> map = new HashMap<String,String>();
		map.put("videoUrl", url);
		map.put("portraitUrl", imagePortraitUrl);
		map.put("landscapeUrl", imageLandscapeUrl);
		return R.success(map);
	}
	@PostMapping("uploadCover")
	public Object uploadCover(@RequestParam("file") MultipartFile file,String rc) throws OssException {
		Long id = idGenerator.getGeneratorId();
		final String filename = id.toString();
		String url = ossService.upload(file, new OssParameter() {
			@Override
			public String getUploadPath() {
				Integer vps = VpsUtils.getVps(filename);
				return dasbxConfig.getBaseImagePath()+"/"+vps;
			}
			@Override
			public String getFilename(String f) {
				return filename+"-"+rc;
			}
		});
		return R.success(url);
	}
}
