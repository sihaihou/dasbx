package com.reyco.dasbx.login.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.reyco.dasbx.login.core.service.ScanQRCodeService;
import com.reyco.dasbx.model.constants.ScanQRCodeType;

public abstract class AbstractScanQRCodeService implements ScanQRCodeService {
	
	@Autowired
	private List<ScanQRCodeService> scanQRCodeServices;
	
	protected ScanQRCodeService getMatchScanQRCodeService(ScanQRCodeType scanQRCodeType) {
		for (ScanQRCodeService scanQRCodeService : scanQRCodeServices) {
			if(scanQRCodeService.support(scanQRCodeType)) {
				return scanQRCodeService;
			}
		}
		return null;
	}
	

}
