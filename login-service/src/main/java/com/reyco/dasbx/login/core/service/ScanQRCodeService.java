package com.reyco.dasbx.login.core.service;

import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.login.core.model.dto.ScanQRCodeDto;
import com.reyco.dasbx.login.core.model.vo.ScanQRCodeInfoVo;
import com.reyco.dasbx.model.constants.ScanQRCodeType;

/**
 * 扫码service
 * @author reyco
 *
 */
public interface ScanQRCodeService {
	
	/**
	 * 
	 * @param scanQRCodeType
	 * @return
	 */
	boolean support(ScanQRCodeType scanQRCodeType) ;
	
	/**
	 * 
	 * @param scanQRCodeDto
	 * @return
	 * @throws AuthenticationException
	 */
	ScanQRCodeInfoVo scan(ScanQRCodeDto scanQRCodeDto) throws AuthenticationException;
}
