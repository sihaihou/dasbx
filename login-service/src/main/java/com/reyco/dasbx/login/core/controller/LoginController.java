package com.reyco.dasbx.login.core.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.ArgumentException;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.redis.RedisUtil;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.log.core.annotation.Syslog;
import com.reyco.dasbx.login.core.model.dto.ConfirmQRcodeDto;
import com.reyco.dasbx.login.core.model.dto.EmailLoginDto;
import com.reyco.dasbx.login.core.model.dto.ScanQRCodeDto;
import com.reyco.dasbx.login.core.model.vo.QRCodeInfoVO;
import com.reyco.dasbx.login.core.model.vo.ScanQRCodeInfoVo;
import com.reyco.dasbx.login.core.service.ConfirmQrcodeService;
import com.reyco.dasbx.login.core.service.LoginQRService;
import com.reyco.dasbx.login.core.service.LoginService;
import com.reyco.dasbx.login.core.service.ScanQRCodeService;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.vo.SysAccountToken;

@RestController
@RequestMapping("/sys")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private LoginQRService loginQRService;
	@Autowired
	@Qualifier("scanQRCodeServiceImpl")
	private ScanQRCodeService scanQRCodeService;
	@Autowired
	private ConfirmQrcodeService confirmQrcodeService;
	@Autowired
	private RedisUtil redisUtil;
	
	//密码登录
	@Syslog
	@PostMapping("login")
	public Object login(String t) throws ArgumentException, AuthenticationException {
		SysAccountToken accountToken = loginService.login(t);
		return R.success(accountToken);
	}
	@DeleteMapping("logout")
	public Object logout(HttpServletRequest request) throws AuthenticationException {
		loginService.logout(request);
		return R.success("登出成功");
	}
	/**
	 * 获取邮箱code
	 * @param email
	 * @return
	 */
	@PostMapping("createEmailCode")
	public Callable<R<?>> createEmailCode(String email) {
		return new Callable<R<?>>() {
			@Override
			public R<?> call() throws Exception {
				loginService.createEmailCode(email);
				return R.success();
			}
		};
	}
	/**
	 * 邮箱登录
	 * @param t
	 * @return
	 * @throws ArgumentException
	 * @throws AuthenticationException
	 * @throws SBException
	 */
	@PostMapping("emailLogin")
	public Object emailLogin(@RequestBody EmailLoginDto emailLoginDto) throws ArgumentException, AuthenticationException {
		SysAccountToken accountToken = loginService.emailLogin(emailLoginDto);
		return R.success(accountToken);
	}
	
	@GetMapping("isLogin")
	public Object isLogin() throws ArgumentException, AuthenticationException {
		Boolean isLogin = loginService.isLogin();
		return R.success(isLogin);
	}
	/**
	 * 获取二维码qrcode
	 * @param deviceId
	 * @param qrcode
	 * @return
	 */
	@GetMapping("getQRCode")
	public Object getQRCode(String qrcode) {
		 QRCodeInfoVO qrCodeInfo = loginQRService.getQRCodeInfo(qrcode);
		 return R.success(qrCodeInfo);
	}
	/**
	 * 扫码
	 * @param scanQRCodeDto
	 * @return
	 * @throws AuthenticationException
	 */
	@PostMapping("scanQRCode")
	public Object scanQRCode(@RequestBody ScanQRCodeDto scanQRCodeDto) throws AuthenticationException  {
		ScanQRCodeInfoVo scanQRCodeInfoVo = scanQRCodeService.scan(scanQRCodeDto);
		return R.success(scanQRCodeInfoVo);
	}
	/**
	 * 确认登录
	 * @param confirmQRcodeDto
	 * @throws AuthenticationException
	 */
	@PostMapping("confirmQrcode")
	public void confirmQrcode(@RequestBody ConfirmQRcodeDto confirmQRcodeDto) throws AuthenticationException {
		confirmQrcodeService.confirmQrcode(confirmQRcodeDto);
	}
	/**
	 * 获取token
	 * @param qrcode
	 * @return
	 * @throws AuthenticationException
	 */
	@GetMapping("getToken")
	public Object getToken(String qrcode) throws AuthenticationException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String deviceId = TokenUtils.getDeviceId(request);
		String deviceType = TokenUtils.getDeviceType(request);
		String token = redisUtil.get(CachePrefixConstants.QR_CODE_BING_TOKEN_PREFIX+qrcode);
		//移除login的qrcode
		redisUtil.delete(CachePrefixConstants.QR_CODE_LOGIN_QRID_PREFIX+qrcode);
		//移除qrcode与token的绑定
		redisUtil.delete(CachePrefixConstants.QR_CODE_BING_TOKEN_PREFIX+qrcode);
		SysAccountToken sysAccountToken = TokenUtils.getToken(deviceId,deviceType.toUpperCase(),token);
		return R.success(sysAccountToken);
	}
}
