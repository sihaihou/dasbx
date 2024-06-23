package com.reyco.dasbx.login.core.service;

import com.reyco.dasbx.login.core.model.vo.QRCodeInfoVO;

/**
 * 系统服务
 * @author reyco
 *
 */
public interface SysService {
	
	/**
	 * 生成浏览器唯一code
	 * @param code
	 * @return
	 */
	Long getCode(Long code);
}
