package com.reyco.dasbx.login.core.service;

import com.reyco.dasbx.commons.exception.AuthenticationException;
import com.reyco.dasbx.login.core.model.dto.ConfirmQRcodeDto;

public interface ConfirmQrcodeService {
	
	void confirmQrcode(ConfirmQRcodeDto confirmQRcodeDto) throws AuthenticationException;
}
