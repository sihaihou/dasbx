package com.reyco.dasbx.id.properties;

import java.util.Random;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(SnowFlakeProperties.SNOWFLAKE_PREFIX)
public class SnowFlakeProperties {
	
	public final static String SNOWFLAKE_PREFIX = "reyco.dasbx.snowflake";
	
	private Random random = new Random();
	
	private Long machineId = (long) random.nextInt(32);
	
	private Long workId = (long) random.nextInt(32);
	
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
}
