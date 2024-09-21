package com.reyco.dasbx.id.core;

import org.springframework.beans.factory.InitializingBean;

import com.reyco.dasbx.id.core.exception.IdGeneratorException;
import com.reyco.dasbx.id.properties.SnowFlakeProperties;

/**
 * 雪花算法Id生成器
 * @author reyco
 *
 */
public class SnowflakeIdGenerator implements IdGenerator<Long>, InitializingBean {

	private SnowFlakeProperties snowFlakeProperties;

	private SnowFlake snowFlake;
	
	public SnowflakeIdGenerator(SnowFlakeProperties snowFlakeProperties) {
		super();
		this.snowFlakeProperties = snowFlakeProperties;
	}
	@Override
	public Long getGeneratorId() {
		return snowFlake.nextId();
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		Long machineId = snowFlakeProperties.getMachineId();
		if (machineId > 31 || machineId < 0) {
			throw new IdGeneratorException("MachineId out of bounds,machineId is between 0 and 31.");
		}
		Long workId = snowFlakeProperties.getWorkId();
		if (workId > 31 || workId < 0) {
			throw new IdGeneratorException("WorkId out of bounds,workId is between 0 and 31.");
		}
		snowFlake = new SnowFlake(machineId, workId);
	}
	public SnowFlakeProperties getSnowFlakeProperties() {
		return snowFlakeProperties;
	}
	public void setSnowFlakeProperties(SnowFlakeProperties snowFlakeProperties) {
		this.snowFlakeProperties = snowFlakeProperties;
	}
}
