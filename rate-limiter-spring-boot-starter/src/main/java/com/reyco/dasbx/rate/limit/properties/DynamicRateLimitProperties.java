package com.reyco.dasbx.rate.limit.properties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = DynamicRateLimitProperties.RATE_LIMIT)
public class DynamicRateLimitProperties {
	
	public final static String RATE_LIMIT = "reyco.dasbx.rate-limiter";
	/**
     * 默认配置
     */
    private DefaultConfig defaultConfig = new DefaultConfig();
    
    /**
     * 各业务配置
     */
    private Map<String, ResouresConfig> configs = new ConcurrentHashMap<>();
	
    public DynamicRateLimitProperties() {
	}
	public DynamicRateLimitProperties(DefaultConfig defaultConfig, Map<String, ResouresConfig> configs) {
		super();
		this.defaultConfig = defaultConfig;
		this.configs = configs;
	}
	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}
	public void setDefaultConfig(DefaultConfig defaultConfig) {
		this.defaultConfig = defaultConfig;
	}
	public Map<String, ResouresConfig> getConfigs() {
		return configs;
	}
	public void setConfigs(Map<String, ResouresConfig> configs) {
		this.configs = configs;
	}

	public static class DefaultConfig {
        private int window = 1;
        private TimeUnit unit = TimeUnit.SECONDS;
        private int maxRequests = 100;
        private String description = "描述资源信息";
        public DefaultConfig() {
			// TODO Auto-generated constructor stub
		}
		public DefaultConfig(int window, TimeUnit unit, int maxRequests,String description) {
			super();
			this.window = window;
			this.unit = unit;
			this.maxRequests = maxRequests;
			this.description = description;
		}
		public int getWindow() {
			return window;
		}
		public void setWindow(int window) {
			this.window = window;
		}
		public int getMaxRequests() {
			return maxRequests;
		}
		public void setMaxRequests(int maxRequests) {
			this.maxRequests = maxRequests;
		}
		public TimeUnit getUnit() {
			return unit;
		}
		public void setUnit(TimeUnit unit) {
			this.unit = unit;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
    }
}
