package com.reyco.dasbx.id.properties;

import java.net.InetAddress;
import java.security.SecureRandom;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(SnowFlakeProperties.SNOWFLAKE_PREFIX)
public class SnowFlakeProperties {
	
	public final static String SNOWFLAKE_PREFIX = "reyco.dasbx.snowflake";
	
	private final static SecureRandom RANDOM = new SecureRandom();
	
	private Long machineId = (long) getMachineIdByIp();
	
	private Long workId = (long) RANDOM.nextInt(32);
	
	public Long getMachineId() {
		return machineId;
	}
	public void setMachineId(Long machineId) {
		this.machineId = machineId;
	}
	public Long getWorkId() {
		return workId;
	}
	public void setWorkId(Long workId) {
		this.workId = workId;
	}
	
	/**
	 * 根据本地 IP 计算默认 MachineId
	 */
	private static int getMachineIdByIp() {
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			return Math.abs(ip.hashCode()) % 32;
		} catch (Exception e) {
			// 如果获取 IP 失败，降级使用随机数
			return RANDOM.nextInt(32);
		}
	}
}
