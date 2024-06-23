package com.reyco.dasbx.config.initializer;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.Ordered;

public class RegisterTextEncryptorListener implements ApplicationListener<ApplicationPreparedEvent>, Ordered {
	
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
                beanFactory.registerSingleton(TEXT_ENCRYPTOR_NAME,new DasbxTextEncryptor());
            }
        }
	}
}
