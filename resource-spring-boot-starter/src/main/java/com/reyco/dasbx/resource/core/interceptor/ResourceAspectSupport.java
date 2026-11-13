package com.reyco.dasbx.resource.core.interceptor;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.Executor;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.reyco.dasbx.commons.utils.KeyValueMergeUtils;
import com.reyco.dasbx.commons.utils.UnwrapUtils;
import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.commons.utils.net.RequestUtils;
import com.reyco.dasbx.resource.core.handler.ResourceHandler;
import com.reyco.dasbx.resource.core.model.DefaultResourceDefinition;
import com.reyco.dasbx.resource.core.support.ResourceParameterMasker;
import com.reyco.dasbx.trim.requset.RemoveSpaceHttpServletRequestWrapper;

public abstract class ResourceAspectSupport implements InitializingBean{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
	
	private ResourceAttributeSource resourceAttributeSource;
	
	protected AnnotationAttributes enableResourceAttributes;

	private ResourceHandler resourceHandler;
	
	private String applicationName;
	
	protected Executor executor;

	private Boolean annotation = false;
	
	private ResourceParameterMasker parameterMasker;
	
	public Executor getExecutor() {
		return executor;
	}
	public void setExecutor(Executor executor) {
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
		 this.annotation = Boolean.TRUE.equals(annotation);
	}
	public ResourceParameterMasker getParameterMasker() {
	    return parameterMasker;
	}
	public void setParameterMasker(ResourceParameterMasker parameterMasker) {
	    this.parameterMasker = parameterMasker;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (annotation && getResourceAttributeSource() == null) {
			throw new IllegalStateException("resourceAttributeSource is required when annotation mode is enabled.");
		}
		if (parameterMasker == null) {
		    throw new IllegalStateException("ResourceParameterMasker is required");
		}
	}
	private ResourceAttribute getResourceAttribute(ResourceAttributeSource resourceAttributeSource, Method method,
			Class<?> targetClass) {
		if (resourceAttributeSource == null || method == null || targetClass == null) {
	        return null;
	    }
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
		} catch (Throwable e) {
			throwable = e;
			throw e;
		} finally {
			
			long endTime = System.currentTimeMillis();
			
			String token = null;
			try {
	            token = parseToken(method, targetClass);
	        } catch (Throwable e) {
	            logger.warn("采集 Token 失败: {}", e.getMessage());
	        }
			
			Map<String, Object> parameters = new LinkedHashMap<String, Object>();
			try {
	            Map<String, Object> parsed = parseParameter(method, targetClass, args);
	            if (parsed != null) {
	                parameters = parsed;
	            }
	        } catch (Throwable e) {
	            logger.warn("采集请求参数失败，使用空参数继续记录日志: {}", e.getMessage());
	        }
			
			final String currentToken = token;
	        final Map<String, Object> currentParameters = parameters;
	        try {
	            if (executor == null) {
	                handlerDefaultResourceDefinition(method, targetClass, args,
	                        startTime, endTime, throwable, currentToken, currentParameters);
	            } else {
	                executor.execute(new ResourceHandlerThread(method, targetClass, args,
	                        startTime, endTime, throwable, currentToken, currentParameters));
	            }
	        } catch (Throwable e) {
	            logger.warn("提交资源日志任务失败，改为同步处理: {}", e.getMessage());
	            handlerDefaultResourceDefinition(method, targetClass, args,startTime, endTime, throwable, currentToken, currentParameters);
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
		DefaultResourceDefinition resourceDefinition = new DefaultResourceDefinition();
		resourceDefinition.setStartTime(startTime);
		resourceDefinition.setEndTime(endTime);
		resourceDefinition.setSuccess(throwable == null);
		resourceDefinition.setApplication(applicationName);
		
		String message;
		if(throwable!=null && StringUtils.isNotBlank(message=throwable.getMessage())) {
			String desensitize = parameterMasker.maskText(message);
			resourceDefinition.setExceptionInfo(desensitize);
		}
		
		try {
			String methodName = getMethodName(method, targetClass);
	        resourceDefinition.setMethod(methodName);
	    } catch (Throwable e) {
	        logger.warn("解析资源方法失败: {}", e.getMessage());
	        resourceDefinition.setMethod(method == null ? "" : method.getName());
	    }
		
		try {
			String description = parseDescription(method, targetClass,parameters);
	        resourceDefinition.setDescription(description);
	    } catch (Throwable e) {
	        logger.warn("解析资源描述失败: {}", e.getMessage());
	        resourceDefinition.setDescription("");
	    }
		
		try {
			String resourceName = parseResource(method, targetClass,parameters);
	        resourceDefinition.setResource(resourceName);
	    } catch (Throwable e) {
	        logger.warn("解析资源路径失败: {}", e.getMessage());
	        resourceDefinition.setResource("");
	    }
		
		try {
			Boolean isController = isController(method, targetClass);
	        resourceDefinition.setIsController(isController);
	    } catch (Throwable e) {
	        resourceDefinition.setIsController(false);
	    }
		
		Map<String, Object> maskedParameters = parameterMasker.maskParameters(parameters);
		resourceDefinition.setToken(parameterMasker.maskToken(token));
		resourceDefinition.setParameters(maskedParameters);
		
		if (resourceHandler == null) {
	        logger.warn("ResourceHandler 未配置，资源日志未处理");
	        return;
	    }
		
		try {
			resourceHandler.handler(resourceDefinition);
		} catch (Throwable e) {
			logger.warn("处理系统日志出错！exception:{}", e);
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
			if (request != null) {
	            Map<String, Object> parameters = new LinkedHashMap<String, Object>();

	            // getParameterMap 已经完成 URL 解码，并保留重复参数。
	            addRequestParameters(parameters, request.getParameterMap());

	            // JSON 请求体放在后面，同名字段保持原有的“请求体覆盖查询参数”规则。
	            addJsonBodyParameters(parameters, request);

	            if (!parameters.isEmpty()) {
	                return parameters;
	            }
	        }
		}
		// GET请求
		return parseMethodParameters(method, args);
	}
	/**
	 * 添加 Servlet 请求参数。
	 */
	private void addRequestParameters(Map<String, Object> target, Map<String, String[]> requestParameters) {
	    if (target == null || requestParameters == null || requestParameters.isEmpty()) {
	        return;
	    }

	    for (Map.Entry<String, String[]> entry : requestParameters.entrySet()) {
	        String name = entry.getKey();
	        if (StringUtils.isBlank(name)) {
	            continue;
	        }

	        String[] values = entry.getValue();
	        if (values == null || values.length == 0) {
	            target.put(name, "");
	        } else if (values.length == 1) {
	            target.put(name, values[0]);
	        } else {
	            // 保留重复参数，例如 tags=a&tags=b。
	            target.put(name, Arrays.asList(values));
	        }
	    }
	}
	/**
	 * 读取 JSON 请求体参数。
	 * trim 功能关闭或请求没有被包装时直接跳过，不再做强制类型转换。
	 */
	private void addJsonBodyParameters(Map<String, Object> target, HttpServletRequest request) {
	    if (!(request instanceof RemoveSpaceHttpServletRequestWrapper)) {
	        return;
	    }

	    String body = ((RemoveSpaceHttpServletRequestWrapper) request).getBody();
	    if (StringUtils.isBlank(body)) {
	        return;
	    }

	    try {
	        Map<String, Object> bodyParameters = JsonUtils.jsonToMap(body);
	        if (bodyParameters != null && !bodyParameters.isEmpty()) {
	            target.putAll(bodyParameters);
	        }
	    } catch (RuntimeException e) {
	        // 请求体不是 JSON 对象或 JSON 格式非法时，不影响其他参数采集。
	        logger.debug("解析请求体参数失败，回退到方法参数: {}", e.getMessage());
	    }
	}
	/**
	 * 将从方法参数中采集的数据作为兜底。
	 */
	private Map<String, Object> parseMethodParameters(Method method, Object[] args) {
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
	    if (args == null || args.length == 0) {
	        return parameters;
	    }
	    String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
	    if (parameterNames == null || parameterNames.length != args.length) {
	        return parameters;
	    }
	    for (int i = 0; i < args.length; i++) {
	        Object value = args[i];
	        if (value == null || isFrameworkArgument(value)) {
	            continue;
	        }

	        Object converted = KeyValueMergeUtils.values(value);
	        if (converted != null) {
	            parameters.put(parameterNames[i], converted);
	        }
	    }
	    return parameters;
	}
	private boolean isFrameworkArgument(Object value) {
	    if (value instanceof ServletRequest
	            || value instanceof ServletResponse
	            || value instanceof MultipartFile
	            || value instanceof Principal
	            || value instanceof InputStream
	            || value instanceof OutputStream
	            || value instanceof Reader
	            || value instanceof Writer) {
	        return true;
	    }

	    String className = value.getClass().getName();
	    return className.startsWith("org.springframework.")
	            || className.startsWith("javax.servlet.")
	            || className.startsWith("jakarta.servlet.")
	            || className.startsWith("org.apache.catalina.")
	            || className.startsWith("org.apache.coyote.");
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
			if(requestMethod.length>0) {
				return requestMethod[0].name();
			}
			return "GET";
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
		if (request == null || enableResourceAttributes == null) {
	        return null;
	    }
		
		String[] tokenNames = enableResourceAttributes.getStringArray("tokenNames");
		if (tokenNames == null || tokenNames.length == 0) {
	        return null;
	    }

		for (String tokenName : tokenNames) {
			if (StringUtils.isBlank(tokenName)) {
	            continue;
	        }
			String token = RequestUtils.getTokenByHeaderAndCookie(request, tokenName);
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
