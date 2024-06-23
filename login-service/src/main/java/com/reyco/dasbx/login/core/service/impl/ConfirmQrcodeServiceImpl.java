package com.reyco.dasbx.login.core.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.redis.RedisUtil;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.login.core.model.dto.ConfirmQRcodeDto;
import com.reyco.dasbx.login.core.model.vo.QRCodeInfoVO;
import com.reyco.dasbx.login.core.service.ConfirmQrcodeService;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.model.constants.QRCodeType;
import com.reyco.dasbx.model.vo.SysAccountToken;

@Service
public class ConfirmQrcodeServiceImpl implements ConfirmQrcodeService{
	
	@Autowired
	private RedisUtil redisUtil;
	@Override
	public void confirmQrcode(ConfirmQRcodeDto confirmQRcodeDto) throws AuthenticationException {
		String qrcodeKey = CachePrefixConstants.QR_CODE_LOGIN_QRID_PREFIX+confirmQRcodeDto.getQrcode();
		if(!redisUtil.hasKey(qrcodeKey)) {
			throw new RuntimeException("二维码已过期！");
		}
		String qrCodeInfoVOJson = redisUtil.get(qrcodeKey);
		//校验临时token
		SysAccountToken sysAccountToken = TokenUtils.getTempToken(confirmQRcodeDto.getQrcode(), confirmQRcodeDto.getToken());
		if(sysAccountToken==null) {
			throw new RuntimeException("登录设备和扫码设备不一致！");
		}
		//移除临时token
		TokenUtils.removeTempToken(confirmQRcodeDto.getQrcode(), sysAccountToken.getToken());
		QRCodeInfoVO qrCodeInfoVO = JsonUtils.jsonToObj(qrCodeInfoVOJson, QRCodeInfoVO.class);
		//创建PC端token
		sysAccountToken.setToken(qrCodeInfoVO.getCode());
		TokenUtils.createToken(qrCodeInfoVO.getDeviceId(), Constants.DEVICE_PC_TYPE.toUpperCase(), sysAccountToken);
		//绑定qrcode和token关系
		redisUtil.set(CachePrefixConstants.QR_CODE_BING_TOKEN_PREFIX+qrCodeInfoVO.getQrcode(), sysAccountToken.getToken(),Constants.TEMP_TOKEN_EXPIRES_TIME,TimeUnit.MILLISECONDS);
		//二维码已确认登录
		qrCodeInfoVO.setState(QRCodeType.CONFIRMED.getState());
		redisUtil.set(qrcodeKey, JsonUtils.objToJson(qrCodeInfoVO), 1*60*1000, TimeUnit.MILLISECONDS);
	}
	

}
