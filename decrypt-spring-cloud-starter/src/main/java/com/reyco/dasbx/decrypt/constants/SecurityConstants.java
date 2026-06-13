package com.reyco.dasbx.decrypt.constants;

/**
 * 配置信息
 * @author reyco
 *
 */
public class SecurityConstants {
	
	private SecurityConstants() {
		throw new RuntimeException("禁止实例化");
	}
	
	/** 配置前缀 */
    public static final String PREFIX = "reyco.dasbx.security";
    
    /** 具体配置项 */
    public static final String ALGORITHM_NAME = PREFIX + ".algorithm-name";
    public static final String SECRET = PREFIX + ".secret";
    public static final String PUBLIC_KEY = PREFIX + ".public-key";
    public static final String PRIVATE_KEY = PREFIX + ".private-key";
    
}
