package com.reyco.dasbx.config.web;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.reyco.dasbx.config.web.interceptor.AuthInterceptor;
import com.reyco.dasbx.config.web.interceptor.SecurityInterceptor;
import com.reyco.dasbx.config.web.interceptor.SignInterceptor;
import com.reyco.dasbx.config.web.interceptor.properteies.Interceptor;
import com.reyco.dasbx.config.web.interceptor.properteies.InterceptorProperties;

@Component
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	private SecurityInterceptor securityInterceptor;
	@Autowired
	private AuthInterceptor authInterceptor;
	@Autowired
	private SignInterceptor signInterceptor;
	@Autowired
	private InterceptorProperties interceptorProperties;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		Map<String, Interceptor> interceptors = interceptorProperties.getInterceptors();
		Interceptor securityInterceptorConfig = interceptors.get(SecurityInterceptor.INTERCEPTOR_NAME);
		Interceptor authInterceptorConfig = interceptors.get(AuthInterceptor.INTERCEPTOR_NAME);
		//Interceptor signInterceptorConfig = interceptors.get(SignInterceptor.INTERCEPTOR_NAME);
		int securityOrder = securityInterceptorConfig.getOrder()==null?-3:securityInterceptorConfig.getOrder().intValue();
		int authOrder = authInterceptorConfig.getOrder()==null?-2:authInterceptorConfig.getOrder().intValue();
		//int signyOrder = signInterceptorConfig.getOrder()==null?-1:signInterceptorConfig.getOrder().intValue();
		registry.addInterceptor(securityInterceptor).excludePathPatterns(securityInterceptorConfig.getExclude()).order(securityOrder);
		registry.addInterceptor(authInterceptor).excludePathPatterns(authInterceptorConfig.getExclude()).order(authOrder);
		//registry.addInterceptor(signInterceptor).excludePathPatterns(signInterceptorConfig.getExclude()).order(signyOrder);
	}
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowCredentials(true)
		.allowedOrigins("*")
		.allowedMethods("GET","POST","PUT","PATCH","DELETE","HEAD","OPTIONS")
		.maxAge(3600);
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("/**")
         .addResourceLocations("classpath:/static/");
	}
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		 configurer.setDefaultTimeout(60 * 1000L);
	        configurer.registerCallableInterceptors(timeoutInterceptor());
	        configurer.setTaskExecutor(asyncTaskExecutor());
	}
	@Bean
    public TimeoutCallableProcessingInterceptor timeoutInterceptor() {
        return new TimeoutCallableProcessingInterceptor();
    }
	@Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(20000);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("com.reyco.dasbx.portal.asyncMvcThread");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
