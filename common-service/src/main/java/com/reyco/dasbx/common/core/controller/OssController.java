package com.reyco.dasbx.common.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.DasbxConfig;
import com.reyco.dasbx.oss.core.OssParameter;
import com.reyco.dasbx.oss.core.OssService;
import com.reyco.dasbx.oss.core.exception.OssException;

@RestController
@RequestMapping("oss")
public class OssController {
	
	@Autowired
	private OssService OssService;
	@Autowired
	private DasbxConfig dasbxConfig;
	
	@PostMapping("upload")
	public Object upload(@RequestParam("file") MultipartFile file) throws OssException {
		String upload = OssService.upload(file, new OssParameter() {
			@Override
			public String getUploadPath() {
				return dasbxConfig.getBasePath();
			}
		});
		return R.success(upload);
	}
}
