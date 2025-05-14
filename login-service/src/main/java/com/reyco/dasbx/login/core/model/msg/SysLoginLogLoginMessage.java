package com.reyco.dasbx.login.core.model.msg;

import com.reyco.dasbx.commons.utils.serializable.ToString;
import com.reyco.dasbx.rabbitmq.model.RabbitMessage;

public class SysLoginLogLoginMessage extends ToString  implements RabbitMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6232602261024646545L;
	private Long code;
	private Long userId;
	private String username;
	private String loginDevice;
	private String loginIp;
	private String loginCity;
	private Long gmtLogin;
	@Override
	public String getCorrelationDataId() {
		return code.toString();
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLoginDevice() {
		return loginDevice;
	}
	public void setLoginDevice(String loginDevice) {
		this.loginDevice = loginDevice;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginCity() {
		return loginCity;
	}
	public void setLoginCity(String loginCity) {
		this.loginCity = loginCity;
	}
	public Long getGmtLogin() {
		return gmtLogin;
	}
	public void setGmtLogin(Long gmtLogin) {
		this.gmtLogin = gmtLogin;
	}
}
