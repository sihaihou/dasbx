package com.reyco.dasbx.login.core.service.impl;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.config.redis.RedisUtil;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.id.core.IdGenerator;
import com.reyco.dasbx.login.core.model.vo.QRCodeInfoVO;
import com.reyco.dasbx.login.core.service.LoginQRService;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.QRCodeType;

@Service
public class LoginQRServiceImpl implements LoginQRService {
	@Autowired
	private IdGenerator<Long> idGenerator;
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public QRCodeInfoVO getQRCodeInfo(String qrcode) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String deviceId = TokenUtils.getDeviceId(request);
		String code = TokenUtils.getCodeString(request);
		QRCodeInfoVO qrCodeInfoVO = null;
		if(StringUtils.isBlank(qrcode)) {
			qrcode = idGenerator.getGeneratorId().toString();
			qrCodeInfoVO = new QRCodeInfoVO();
			qrCodeInfoVO.setQrcode(qrcode);
			qrCodeInfoVO.setDeviceId(deviceId);
			qrCodeInfoVO.setState(QRCodeType.UNSCAN.getState());
			qrCodeInfoVO.setCode(code);
			redisUtil.set(CachePrefixConstants.QR_CODE_LOGIN_QRID_PREFIX+qrcode, JsonUtils.objToJson(qrCodeInfoVO), 1*60*1000, TimeUnit.MILLISECONDS);
		}
		if(qrCodeInfoVO!=null) {
			return qrCodeInfoVO;
		}
		String qrCodeInfoVOJson = redisUtil.get(CachePrefixConstants.QR_CODE_LOGIN_QRID_PREFIX+qrcode);
		if(StringUtils.isBlank(qrCodeInfoVOJson)) {
			qrCodeInfoVO = new QRCodeInfoVO();
			qrCodeInfoVO.setQrcode(qrcode);
			qrCodeInfoVO.setDeviceId(deviceId);
			qrCodeInfoVO.setState(QRCodeType.EXPIRED.getState());
			return qrCodeInfoVO;
		}
		qrCodeInfoVO = JsonUtils.jsonToObj(qrCodeInfoVOJson, QRCodeInfoVO.class);
		if(!qrCodeInfoVO.getDeviceId().equals(deviceId)) {
			qrCodeInfoVO = new QRCodeInfoVO();
			qrCodeInfoVO.setQrcode(qrcode);
			qrCodeInfoVO.setDeviceId(deviceId);
			qrCodeInfoVO.setState(QRCodeType.EXPIRED.getState());
			return qrCodeInfoVO;
		}
		return qrCodeInfoVO;
	}
}
