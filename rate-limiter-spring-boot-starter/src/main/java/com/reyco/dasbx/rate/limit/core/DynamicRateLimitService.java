package com.reyco.dasbx.rate.limit.core;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.rate.limit.properties.DynamicRateLimitProperties;
import com.reyco.dasbx.rate.limit.properties.ResouresConfig;


public class DynamicRateLimitService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private DynamicRateLimitProperties dynamicRateLimitProperties;
    
    public DynamicRateLimitService() {
	}
    public DynamicRateLimitService(DynamicRateLimitProperties dynamicRateLimitProperties) {
		super();
		this.dynamicRateLimitProperties = dynamicRateLimitProperties;
	}
    public void setDynamicRateLimitProperties(DynamicRateLimitProperties dynamicRateLimitProperties) {
		this.dynamicRateLimitProperties = dynamicRateLimitProperties;
	}
	/**
     * 获取限流配置
     * 优先级：配置中心配置 > 默认配置
     */
    public RateLimitConfig getConfig(String resoures) {
        // 1. 从配置中心获取
        RateLimitConfig propertiesConfig = getPropertiesConfig(resoures);
        if (propertiesConfig != null) {
        	logger.debug("Using properties config for Resoures: {}", resoures);
            return propertiesConfig;
        }
        // 2. 返回默认配置
        logger.debug("Using default config for Resoures: {}", resoures);
        return getDefaultConfig();
    }
    /**
     * 从配置中心获取配置
     */
    private RateLimitConfig getPropertiesConfig(String resoures) {
        try {
            ResouresConfig resouresConfig = dynamicRateLimitProperties.getConfigs().get(resoures);
            if (resouresConfig != null) {
                return new RateLimitConfig(
                		resouresConfig.getWindow(),
                		resouresConfig.getMaxRequests(),
                		TimeUnit.valueOf(resouresConfig.getUnit())
                );
            }
        } catch (Exception e) {
        	logger.error("Failed to get properties config for Resoures: {}", resoures, e);
        }
        return null;
    }
    
    /**
     * 获取默认配置
     */
    private RateLimitConfig getDefaultConfig() {
        DynamicRateLimitProperties.DefaultConfig defaultConfig = dynamicRateLimitProperties.getDefaultConfig();
        return new RateLimitConfig(
            defaultConfig.getWindow(),
            defaultConfig.getMaxRequests(),
            defaultConfig.getUnit()
        );
    }
 
}