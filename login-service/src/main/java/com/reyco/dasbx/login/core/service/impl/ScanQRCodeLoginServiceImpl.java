package com.reyco.dasbx.login.core.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.redis.RedisUtil;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.login.core.model.dto.ScanQRCodeDto;
import com.reyco.dasbx.login.core.model.vo.QRCodeInfoVO;
import com.reyco.dasbx.login.core.model.vo.ScanQRCodeInfoVo;
import com.reyco.dasbx.login.core.service.ScanQRCodeService;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.QRCodeType;
import com.reyco.dasbx.model.constants.ScanQRCodeType;
import com.reyco.dasbx.model.vo.SysAccountToken;

@Service
public class ScanQRCodeLoginServiceImpl implements ScanQRCodeService{

	@Autowired
	private RedisUtil redisUtil;
	@Override
	public ScanQRCodeInfoVo scan(ScanQRCodeDto scanQRCodeDto) throws AuthenticationException {
		String qrcodeKey = CachePrefixConstants.QR_CODE_LOGIN_QRID_PREFIX+scanQRCodeDto.getQrcode();
		if(!redisUtil.hasKey(qrcodeKey)) {
			throw new RuntimeException("二维码已过期");
		}
		//获取qrcode
		String qrcodeInfoJson = redisUtil.get(qrcodeKey);
		QRCodeInfoVO qrCodeInfoVO = JsonUtils.jsonToObj(qrcodeInfoJson, QRCodeInfoVO.class);
		
		//创建临时token
		SysAccountToken token = TokenUtils.getToken();
		TokenUtils.createTempToken(scanQRCodeDto.getQrcode(),token);
		
		//已扫码待确认
		qrCodeInfoVO.setState(QRCodeType.TO_BE_CONFIRMED.getState());
		redisUtil.set(qrcodeKey, JsonUtils.objToJson(qrCodeInfoVO), 1*60*1000, TimeUnit.MILLISECONDS);
		
		//返回token
		ScanQRCodeInfoVo scanQRCodeInfoVo = new ScanQRCodeInfoVo();
		scanQRCodeInfoVo.setType(ScanQRCodeType.LOGIN.getType());
		scanQRCodeInfoVo.setToken(token.getToken());
		return scanQRCodeInfoVo;
	}

	@Override
	public boolean support(ScanQRCodeType scanQRCodeType) {
		return ScanQRCodeType.LOGIN == scanQRCodeType;
	}

}
