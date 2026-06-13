package com.reyco.dasbx.login.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.login.core.service.SysService;
import com.reyco.dasbx.login.core.service.security.AesService;
import com.reyco.dasbx.login.core.service.security.RsaService;

@RestController
@RequestMapping("sys")
public class SysController {
	
	@Autowired
	private SysService sysService;

	@Autowired
	private RsaService rsaService;
	
	@Autowired
	private AesService aesService;
	
	@GetMapping("code")
	public Object code(Long code) {
		 Long sysCode = sysService.getCode(code);
		 return R.success(sysCode);
	}
	
	/**
	 * 获取AES密钥
	 * @return
	 * @throws Exception
	 */
	@GetMapping("getSecret")
	public Object getLoginSecret() throws Exception {
		Object secret = aesService.getSecret();
		return R.success(secret);
	}
	/**
	 * 获取公钥
	 * @return
	 * @throws Exception
	 */
	@GetMapping("getPublicKey")
	public Object getPublicKey() throws Exception {
		String publicKey = rsaService.getPublicKey();
		return R.success(publicKey);
	}
}
