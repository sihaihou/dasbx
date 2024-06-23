package com.reyco.dasbx.log.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(SyslogProperties.SYSLOG_PREFIX)
public class SyslogProperties {
	
	public final static String SYSLOG_PREFIX = "reyco.dasbx.syslog";
	
	private int order;
	
	private String[] uniqueIds = new String[] {"authentication"};
	
	private List<String> exclude = new ArrayList<String>();
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String[] getUniqueIds() {
		return uniqueIds;
	}
	public void setUniqueIds(String[] uniqueIds) {
		this.uniqueIds = uniqueIds;
	}
	public List<String> getExclude() {
		return exclude;
	}
	public void setExclude(List<String> exclude) {
		this.exclude = exclude;
	}
}
