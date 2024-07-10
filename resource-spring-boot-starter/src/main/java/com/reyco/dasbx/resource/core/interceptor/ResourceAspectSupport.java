package com.reyco.dasbx.resource.core.interceptor;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.reyco.dasbx.model.constants.MethodType;
import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.commons.utils.KeyValueMergeUtils;
import com.reyco.dasbx.commons.utils.RequestUtils;
import com.reyco.dasbx.commons.utils.UnwrapUtils;
import com.reyco.dasbx.resource.core.handler.ResourceHandler;
import com.reyco.dasbx.resource.core.model.DefaultResourceDefinition;
import com.reyco.dasbx.trim.requset.RemoveSpaceHttpServletRequestWrapper;

public abstract class ResourceAspectSupport implements InitializingBean{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private ResourceAttributeSource resourceAttributeSource;
	
	protected AnnotationAttributes enableResourceAttributes;

	private ResourceHandler resourceHandler;
	
	private String applicationName;
	
	protected ThreadPoolExecutor executor;

	private Boolean annotation = false;
	
	
	public ThreadPoolExecutor getExecutor() {
		return executor;
	}
	public void setExecutor(ThreadPoolExecutor executor) {
		this.executor = executor;
	}
	public ResourceAttributeSource getResourceAttributeSource() {
		return resourceAttributeSource;
	}
	public void setResourceAttributeSource(ResourceAttributeSource resourceAttributeSource) {
		this.resourceAttributeSource = resourceAttributeSource;
	}
	public AnnotationAttributes getEnableResourceAttributes() {
		return enableResourceAttributes;
	}
	public void setEnableResourceAttributes(AnnotationAttributes enableResourceAttributes) {
		this.enableResourceAttributes = enableResourceAttributes;
	}
	public void setResourceHandler(ResourceHandler resourceHandler) {
		this.resourceHandler = resourceHandler;
	}
	public ResourceHandler getResourceHandler() {
		return resourceHandler;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public Boolean getAnnotation() {
		return annotation;
	}
	public void setAnnotation(Boolean annotation) {
		this.annotation = annotation;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		if (annotation && getResourceAttributeSource() == null) {
			throw new IllegalStateException("Either 'resourceAttributeSource' or 'resourceAttributes' is required: "
					+ "If there are no router methods, then don't use a router aspect.");
		}
	}
	private ResourceAttribute getResourceAttribute(ResourceAttributeSource resourceAttributeSource, Method method,
			Class<?> targetClass) {
		return resourceAttributeSource.getResourceAttribute(method, targetClass);
	}
	@Nullable
	protected Object invokeWithinRouter(Method method, @Nullable Class<?> targetClass, Object[] args,
			final InvocationCallback invocation) throws Throwable {
		long startTime = System.currentTimeMillis();
		Throwable throwable = null;
		Object retVal = null;
		try {
			retVal = invocation.proceedWithInvocation();
		} catch (Exception e) {
			throwable = e;
			throw throwable;
		} finally {
			try {
				long endTime = System.currentTimeMillis();
				String token = parseToken(method, targetClass);
				Map<String, Object> parameters = parseParameter(method, targetClass, args);
				if (executor == null) {
					handlerDefaultResourceDefinition(method,targetClass,args,startTime,endTime,throwable,token,parameters);
				} else {
					executor.execute(new ResourceHandlerThread(method,targetClass,args,startTime,endTime,throwable,token,parameters));
				}
			} catch (Exception e) {
				logger.warn("系统日志出错！exception:{}", e);
				e.printStackTrace();
			}
		}
		return retVal;
	}
	/**
	 * 资源搜集
	 * 
	 * @param method
	 * @param targetClass
	 * @param args
	 */
	protected void handlerDefaultResourceDefinition(Method method,Class<?> targetClass,Object[] args,long startTime,long endTime,Throwable throwable,String token,Map<String, Object> parameters) {
		try {
			DefaultResourceDefinition resourceDefinition = new DefaultResourceDefinition();
			resourceDefinition.setStartTime(startTime);
			resourceDefinition.setEndTime(endTime);
			resourceDefinition.setSuccess(throwable == null ? true : false);
			String message;
			if(throwable!=null && StringUtils.isNotBlank(message=throwable.getMessage())) {
				resourceDefinition.setExceptionInfo(message.length()>1000?message.substring(0, 1000):message);
			}
			resourceDefinition.setToken(token);
			String methodName = getMethodName(method, targetClass);
			String description = parseDescription(method, targetClass,parameters);
			String resourceName = parseResource(method, targetClass,parameters);
			Boolean isController = isController(method, targetClass);
			resourceDefinition.setApplication(applicationName);
			resourceDefinition.setResource(resourceName);
			resourceDefinition.setDescription(description);
			resourceDefinition.setIsController(isController);
			resourceDefinition.setParameters(parameters);
			resourceDefinition.setMethod(methodName);
			try {
				resourceHandler.handler(resourceDefinition);
			} catch (Exception e) {
				logger.warn("处理系统日志出错！exception:{}", e);
				e.printStackTrace();
			}
		} catch (Exception e) {
			logger.warn("搜集系统日志出错！exception:{}", e);
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @author reyco
	 */
	class ResourceHandlerThread implements Runnable {
		private Method method;
		private Class<?> targetClass;
		private Object[] args;
		private long startTime;
		private long endTime;
		private Throwable throwable;
		private String token;
		private Map<String, Object> parameters;
		public ResourceHandlerThread(Method method, Class<?> targetClass, Object[] args,long startTime,long endTime,Throwable throwable,String token,Map<String, Object> parameters) {
			super();
			this.method = method;
			this.targetClass = targetClass;
			this.args = args;
			this.startTime =startTime;
			this.endTime = endTime;
			this.throwable = throwable;
			this.token = token;
			this.parameters = parameters;
		}
		@Override
		public void run() {
			handlerDefaultResourceDefinition(method, targetClass, args,startTime,endTime,throwable,token,parameters);
		}
	}
	/**
	 * 解析
	 * @param method
	 * @param args
	 * @return
	 */
	@Deprecated
	private Map<String, Object> parseParameter(Method method,Class<?> targetClass, Object[] args) {
		if(isController(method, targetClass)) {
			HttpServletRequest request = RequestUtils.getHttpServletRequest();
			Map<String,Object> map = new HashMap<>();
			String parameterForQuery = request.getQueryString();
			if(StringUtils.isNotBlank(parameterForQuery)) {
				StringTokenizer stKeyValue = new StringTokenizer(parameterForQuery, "&");
				while(stKeyValue.hasMoreTokens()) {
					String keyValue = stKeyValue.nextToken();
					String[] temp = keyValue.split("=");
					if(temp.length==2 && StringUtils.isNotBlank(temp[0]) && StringUtils.isNotBlank(temp[1])) {
						map.put(temp[0], temp[1]);
					}
				}
			}
			if(!request.getMethod().equalsIgnoreCase(MethodType.GET.getMethod())
					&& !request.getMethod().equalsIgnoreCase(MethodType.OPTIONS.getMethod())
					&& !request.getMethod().equalsIgnoreCase(MethodType.HEAD.getMethod())) {
				RemoveSpaceHttpServletRequestWrapper remove = (RemoveSpaceHttpServletRequestWrapper) request;
				String body = remove.getBody();
				if(StringUtils.isNotBlank(body)) {
					Map<String, Object> parameters = JsonUtils.jsonToMap(body);
					map.putAll(parameters);
				}
			}
			return map;
		}
		LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
		return KeyValueMergeUtils.merge(parameterNames, args);
	}
	/**
	 * 获取描述
	 * 
	 * @return
	 */
	private String parseDescription(Method method, Class<?> targetClass,Map<String, Object> parameters) {
		final ResourceAttribute resourceAttribute = getResourceAttribute(getResourceAttributeSource(), method,targetClass);
		String description = "";
		if (resourceAttribute != null) {
			description = resourceAttribute.getName();
			if (StringUtils.isNotBlank(description)) {
				return description;
			}
			description = resourceAttribute.getValue();
			if (StringUtils.isNotBlank(description)) {
				return description;
			}
		}
		String className = targetClass.getSimpleName();
		String methodName = method.getName();
		description = className + "." + methodName + "()";
		return description;
	}
	/**
	 * 获取资源
	 * @return
	 */
	private String parseResource(Method method, Class<?> targetClass,Map<String, Object> parameters) {
		final ResourceAttribute resourceAttribute = getResourceAttribute(getResourceAttributeSource(), method,
				targetClass);
		String resource = "";
		if (resourceAttribute != null) {
			resource = resourceAttribute.getResource();
		}
		if (StringUtils.isNotBlank(resource)) {
			return resource;
		}
		if (isController(method, targetClass)) {
			 String parsePath = parsePath(method, targetClass); 
			 return UnwrapUtils.unwrapValue(parsePath, parameters);
		}
		String className = targetClass.getSimpleName();
		String methodName = method.getName();
		resource = className + "." + methodName + "()";
		return resource;
	}
	/**
	 * 获取方法名
	 * @param method
	 * @param targetClass
	 * @return
	 */
	private String getMethodName(Method method, Class<?> targetClass) {
		if (isController(method, targetClass)) {
			AnnotationAttributes methodAnnotationAttributes = AnnotatedElementUtils.findMergedAnnotationAttributes(method,RequestMapping.class, false, false);
			RequestMethod[] requestMethod = (RequestMethod[])methodAnnotationAttributes.get("method");
			return requestMethod[0].name();
		} else {
			return method.getName();
		}
	}
	/**
	 * 解析Path
	 * @param element
	 * @param handlerType
	 * @return
	 */
	private String parsePath(AnnotatedElement element, Class<?> handlerType) {
		StringJoiner sj = new StringJoiner("/", "/", "");
		AnnotationAttributes classAnnotationAttributes = AnnotatedElementUtils.findMergedAnnotationAttributes(handlerType, RequestMapping.class, false, false);
		if (classAnnotationAttributes != null) {
			String[] paths = (String[])classAnnotationAttributes.get("path");
			if(paths!=null && paths.length>0){
				sj.add(paths[0]);
			}
		}
		AnnotationAttributes methodAnnotationAttributes = AnnotatedElementUtils.findMergedAnnotationAttributes(element,RequestMapping.class, false, false);
		if (methodAnnotationAttributes != null) {
			String[] paths = (String[])methodAnnotationAttributes.get("path");
			if(paths!=null && paths.length>0){
				sj.add(paths[0]);
			}
		}
		return sj.toString().replace("//", "/");
	}
	/**
	 * 获取Code信息
	 * 
	 * @param request
	 * @return
	 */
	@Deprecated
	protected String parseToken(Method method, Class<?> targetClass) {
		if (!isController(method, targetClass)) {
			return null;
		}
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		String[] tokenNames = enableResourceAttributes.getStringArray("tokenNames");
		for (int i = 0; i < tokenNames.length; i++) {
			String token = RequestUtils.getTokenByHeaderAndCookie(request, tokenNames[i]);
			if (StringUtils.isNotBlank(token)) {
				return token;
			}
		}
		return null;
	}
	/**
	 * 是否controller
	 * @param method
	 * @param targetClass
	 * @return
	 */
	private Boolean isController(Method method, Class<?> targetClass) {
		AnnotationAttributes controllerAnnotationAttributes = AnnotatedElementUtils.findMergedAnnotationAttributes(targetClass,Controller.class, false, false);
		AnnotationAttributes requestMappingAnnotationAttributes = AnnotatedElementUtils.findMergedAnnotationAttributes(method,RequestMapping.class, false, false);
		if (controllerAnnotationAttributes!=null && requestMappingAnnotationAttributes != null) {
			return true;
		}
		return false;
	}
	@FunctionalInterface
	protected interface InvocationCallback {
		Object proceedWithInvocation() throws Throwable;
	}
}
