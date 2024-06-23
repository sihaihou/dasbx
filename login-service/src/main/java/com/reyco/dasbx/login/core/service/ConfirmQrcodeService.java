package com.reyco.dasbx.login.core.service;

import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.login.core.model.dto.ConfirmQRcodeDto;

public interface ConfirmQrcodeService {
	
	void confirmQrcode(ConfirmQRcodeDto confirmQRcodeDto) throws AuthenticationException;
}
