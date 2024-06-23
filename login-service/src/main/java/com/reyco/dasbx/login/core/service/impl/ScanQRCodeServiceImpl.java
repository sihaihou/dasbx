package com.reyco.dasbx.login.core.service.impl;

import org.springframework.stereotype.Service;

import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.login.core.model.dto.ScanQRCodeDto;
import com.reyco.dasbx.login.core.model.vo.ScanQRCodeInfoVo;
import com.reyco.dasbx.login.core.service.ScanQRCodeService;
import com.reyco.dasbx.model.constants.ScanQRCodeType;

@Service("scanQRCodeServiceImpl")
public class ScanQRCodeServiceImpl extends AbstractScanQRCodeService{
	
	@Override
	public ScanQRCodeInfoVo scan(ScanQRCodeDto scanQRCodeDto) throws AuthenticationException {
		ScanQRCodeType scanQRCodeType = ScanQRCodeType.getScanQRCodeType(scanQRCodeDto.getType());
		ScanQRCodeService scanQRCodeService = getMatchScanQRCodeService(scanQRCodeType);
		return scanQRCodeService.scan(scanQRCodeDto);
	}
	@Override
	public boolean support(ScanQRCodeType scanQRCodeType) {
		return false;
	}
}
