package com.reyco.dasbx.resource.annotation;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.reyco.dasbx.resource.constant.ResourceMode;
import com.reyco.dasbx.resource.core.interceptor.AnnotationResourceAttributeSource;
import com.reyco.dasbx.resource.core.interceptor.ResourceAttributeSource;
import com.reyco.dasbx.resource.core.interceptor.ResourceAttributeSourceAdvisor;
import com.reyco.dasbx.resource.core.interceptor.ResourceInterceptor;
import com.reyco.dasbx.resource.core.interceptor.ResourceJdkRegexpAdvisor;
import com.reyco.dasbx.resource.core.support.ResourceMaskingProperties;
import com.reyco.dasbx.resource.core.support.ResourceParameterMasker;

@Configuration
@EnableConfigurationProperties(ResourceMaskingProperties.class)
@AutoConfigureAfter(ResourceHandlerConfiguration.class)
public class ResourceConfiguration extends AbstractResourceConfiguration{
	
	@Value("${spring.cloud.nacos.discovery.service:${spring.application.name:}}")
	private String applicationName;
	
	@Autowired(required=false)
	@Qualifier("resourceExecutor")
	private Executor executor;
	
	@Autowired
	private ResourceMaskingProperties resourceMaskingProperties;
	
	@Bean(name = "resourceExecutor")
	@ConditionalOnMissingBean(name = "resourceExecutor")
	public ThreadPoolTaskExecutor resourceExecutor() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(availableProcessors);
	    executor.setMaxPoolSize(availableProcessors*2+1);
	    executor.setQueueCapacity(1000);
	    executor.setThreadNamePrefix("com.reyco.dasbx.resource.thread-");
	    executor.setWaitForTasksToCompleteOnShutdown(true);
	    executor.setAwaitTerminationSeconds(10);
	    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	    return executor;
	}
	
	@Bean
	public AbstractPointcutAdvisor resourceAdvisor() {
		ResourceMode mode = getResourceMode();
		if(mode==ResourceMode.EXPRESSION) {
			AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
			advisor.setExpression(getExpression());
			advisor.setAdvice(resourceInterceptor());
			advisor.setOrder(getOrder());
			return advisor;
		}
		if(mode==ResourceMode.REGULAR) {
			ResourceJdkRegexpAdvisor advisor = new ResourceJdkRegexpAdvisor();
			advisor.setPatterns(getPatterns());
			advisor.setExcludedPatterns(getExcludedPatterns());
			advisor.setAdvice(resourceInterceptor());
			advisor.setOrder(getOrder());
			return advisor;
		}
		ResourceAttributeSourceAdvisor advisor = new ResourceAttributeSourceAdvisor();
		advisor.setRouterAttributeSource(resourceAttributeSource());
		advisor.setAdvice(resourceInterceptor());
		advisor.setOrder(getOrder());
		return advisor;
		
	}
	@Bean
	public ResourceAttributeSource resourceAttributeSource() {
		return new AnnotationResourceAttributeSource();
	}
	
	@Bean
	public ResourceInterceptor resourceInterceptor() {
		ResourceInterceptor resourceInterceptor = new ResourceInterceptor();
		if(getResourceMode()==ResourceMode.ANNOTATION) {
			resourceInterceptor.setAnnotation(true);
		}
		resourceInterceptor.setResourceAttributeSource(resourceAttributeSource());
		resourceInterceptor.setEnableResourceAttributes(enableAttributes);
		resourceInterceptor.setResourceHandler(resourceHandler);
		resourceInterceptor.setApplicationName(applicationName);
		resourceInterceptor.setExecutor(executor);
		resourceInterceptor.setParameterMasker(new ResourceParameterMasker(resourceMaskingProperties));
		return resourceInterceptor;
	}
	
	
}
