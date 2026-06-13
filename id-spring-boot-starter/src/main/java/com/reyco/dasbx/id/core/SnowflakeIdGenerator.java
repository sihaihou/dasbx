package com.reyco.dasbx.id.core;

import org.springframework.beans.factory.InitializingBean;

import com.reyco.dasbx.id.core.exception.IdGeneratorException;
import com.reyco.dasbx.id.properties.SnowFlakeProperties;

/**
 * 雪花算法Id生成器
 * @author reyco
 *
 */
public class SnowflakeIdGenerator implements IdGenerator, InitializingBean {

	private SnowFlakeProperties snowFlakeProperties;

	private SnowFlake snowFlake;
	
	public SnowflakeIdGenerator(SnowFlakeProperties snowFlakeProperties) {
		super();
		this.snowFlakeProperties = snowFlakeProperties;
	}
	
	public static String nextIdStr(String prefix) {
		return prefix+nextIdStr(prefix);
	}
	
	@Override
	public String nextIdStr() {
		return String.valueOf(nextId());
	}
	@Override
	public Long nextIdLong() {
		return nextId();
	}
	/**
	 * 如果业务明确需要原生 Long 类型，可以单独调用此方法
	 */
	public Long nextId() {
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
