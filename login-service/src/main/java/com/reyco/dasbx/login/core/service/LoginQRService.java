package com.reyco.dasbx.login.core.service;

import com.reyco.dasbx.login.core.model.vo.QRCodeInfoVO;

/**
 * login二维码
 * @author reyco
 *
 */
public interface LoginQRService {

	/**
	 * 获取二维码
	 * @param qrCode
	 * @return
	 */
	QRCodeInfoVO getQRCodeInfo(String qrCode);
	
	
}
