package com.reyco.dasbx.decrypt.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.util.StringUtils;

import com.reyco.dasbx.decrypt.CustomTextEncryptor;
import com.reyco.dasbx.decrypt.constants.SecurityConstants;

public class RegisterTextEncryptorListener implements ApplicationListener<ApplicationPreparedEvent>, Ordered {
	
	private static Logger logger = LoggerFactory.getLogger(RegisterTextEncryptorListener.class);
	
	private final static String TEXT_ENCRYPTOR_NAME = "textEncryptor";
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public void onApplicationEvent(ApplicationPreparedEvent event) {
		ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        // 这里回往spring IOC 中添加好几次，是因为父子容器的原因，所以要判断一下
        if(applicationContext instanceof AnnotationConfigApplicationContext){
            ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
            // 这里判断是否已经添加过我们自己的解密算法了，没添加才添加，否则跳过
            if(!beanFactory.containsBean(TEXT_ENCRYPTOR_NAME)){
            	ConfigurableEnvironment environment = applicationContext.getEnvironment();
            	TextEncryptor textEncryptor = initTextEncryptor(environment);
                beanFactory.registerSingleton(TEXT_ENCRYPTOR_NAME,textEncryptor);
            }
        }
	}
	
	/**
	 * 初始化文本加密器
	 * @param environment
	 */
	private TextEncryptor initTextEncryptor(ConfigurableEnvironment environment){
	    String algorithmName = getProperty(environment, SecurityConstants.ALGORITHM_NAME, CustomTextEncryptor.DEFAULT_ALGORITHM_NAME);

	    if (CustomTextEncryptor.AES_ALGORITHM_NAME.equalsIgnoreCase(algorithmName)) {
	        String secret = getProperty(environment, SecurityConstants.SECRET, CustomTextEncryptor.DEFAULT_SECRET);
	        return new CustomTextEncryptor(algorithmName, secret);
	    } else if (CustomTextEncryptor.RSA_ALGORITHM_NAME.equalsIgnoreCase(algorithmName)) {
	        String publicKey = getProperty(environment, SecurityConstants.PUBLIC_KEY, CustomTextEncryptor.DEFAULT_PUBLIC_KEY);
	        String privateKey = getProperty(environment, SecurityConstants.PRIVATE_KEY, CustomTextEncryptor.DEFAULT_PRIVATE_KEY);
	        return new CustomTextEncryptor(algorithmName, publicKey, privateKey);
	    } else {
	        throw new RuntimeException("Unsupported encryption algorithm: " + algorithmName + ". Only AES and RSA are supported.");
	    }
	}
	/**
	 * 
	 * @param env
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	private String getProperty(ConfigurableEnvironment env, String key, String defaultValue) {
	    String value = env.getProperty(key);
	    if (StringUtils.isEmpty(value)) {
	    	if(key.equalsIgnoreCase(SecurityConstants.SECRET)
	    		|| key.equalsIgnoreCase(SecurityConstants.PRIVATE_KEY)) {
	    		logger.warn("未检测到安全密钥配置，正在使用默认密钥。请确保这不是生产环境！");
	    	}
	    	value = defaultValue;
	    }
	    return value;
	}
}
