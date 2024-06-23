package com.reyco.dasbx.log.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.reyco.dasbx.commons.utils.DictionaryOrderUtils;
import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.commons.utils.RequestUtils;
import com.reyco.dasbx.log.core.annotation.Syslog;
import com.reyco.dasbx.log.core.handler.SyslogHandler;
import com.reyco.dasbx.log.properties.SyslogProperties;
import com.reyco.dasbx.trim.requset.RemoveSpaceHttpServletRequestWrapper;

@Aspect
public class SyslogAspect implements Ordered {

	private static Logger logger = LoggerFactory.getLogger(SyslogAspect.class);

	private PathMatcher pathMatcher = new AntPathMatcher();

	private SyslogHandler syslogHandler;

	private SyslogProperties syslogProperties;
	
	private String applicationName;
	
	public SyslogAspect() {
	}
	public SyslogAspect(SyslogHandler syslogHandler, SyslogProperties syslogProperties, String applicationName) {
		super();
		this.syslogHandler = syslogHandler;
		this.syslogProperties = syslogProperties;
		this.applicationName = applicationName;
	}
	public SyslogHandler getSyslogHandler() {
		return syslogHandler;
	}
	public void setSyslogHandler(SyslogHandler syslogHandler) {
		this.syslogHandler = syslogHandler;
	}
	public SyslogProperties getSyslogProperties() {
		return syslogProperties;
	}
	public void setSyslogProperties(SyslogProperties syslogProperties) {
		this.syslogProperties = syslogProperties;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	@Override
	public int getOrder() {
		return syslogProperties.getOrder();
	}

	@Pointcut("@annotation(com.reyco.dasbx.log.core.annotation.Syslog)")
	public void syslogAnnotationPointCut() {

	}

	@Pointcut("execution(public * com..controller..*.*(..))")
	public void syslogControllerPointCut() {

	}

	@Around("syslogAnnotationPointCut() || syslogControllerPointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		long beginTime = System.currentTimeMillis();
		Object result = null;
		Exception exception = null;
		try {
			result = joinPoint.proceed();
		} catch (Exception e) {
			exception = e;
			e.printStackTrace();
			throw e;
		} finally {
			try {
				long endTime = System.currentTimeMillis();
				HttpServletRequest request = RequestUtils.getHttpServletRequest();
				boolean flag = false;
				for (String temp : syslogProperties.getExclude()) {
					if (pathMatcher.match(temp, request.getServletPath())) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					SyslogMessage syslogMessage = createSyslogMessage(joinPoint, request, beginTime, endTime, exception);
					try {
						syslogHandler.handler(syslogMessage);
					} catch (Exception e2) {
						logger.warn("An exception occurred during log processing,exception:{}",e2.getMessage());
						e2.printStackTrace();
					}
				}
			} catch (Exception ex) {
				logger.warn("Error creating system log message,exception:{}",ex.getMessage());
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 创建系统消息
	 * 
	 * @param joinPoint
	 * @param request
	 * @param beginTime
	 * @param endTime
	 * @param exception
	 */
	private SyslogMessage createSyslogMessage(ProceedingJoinPoint joinPoint, HttpServletRequest request, long beginTime,
			long endTime, Exception exception) {
		DefaultSyslogMessage syslogMessage = null;
		try {
			syslogMessage = collectSyslogMessage(joinPoint, request, beginTime, endTime);
			if (exception != null) {
				syslogMessage.setSuccess((byte) 0);
			} else {
				syslogMessage.setSuccess((byte) 1);
			}
			if (syslogMessage != null) {
				syslogMessage.setCorrelationDataId(syslogMessage.getCode());
			}
			return syslogMessage;
		} catch (Exception e) {
			logger.warn("搜集系统日志出错！request:{}", request);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 搜集系统日志
	 * 
	 * @param joinPoint
	 * @param request
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws IOException
	 */
	private DefaultSyslogMessage collectSyslogMessage(ProceedingJoinPoint joinPoint, HttpServletRequest request,
			long beginTime, long endTime) throws IOException {
		DefaultSyslogMessage syslogMessage = new DefaultSyslogMessage();
		syslogMessage.setService(applicationName);
		syslogMessage.setGmtRequest(beginTime);
		syslogMessage.setGmtResponse(endTime);
		syslogMessage.setTimes(syslogMessage.getGmtResponse() - syslogMessage.getGmtRequest());
		String code = getSysCode(request);
		String path = request.getServletPath();
		String method = request.getMethod();
		Byte type = getType(method);
		syslogMessage.setCode(code);
		syslogMessage.setPath(path);
		syslogMessage.setMethod(method);
		syslogMessage.setType(type);
		String name = getRequestName(joinPoint);
		syslogMessage.setName(name);
		String params = getRequestParams(request);
		syslogMessage.setParams(params);
		return syslogMessage;
	}

	/**
	 * 0未知 1查看 2创建 3编辑 4删除
	 * 
	 * @param method
	 * @return
	 */
	private Byte getType(String method) {
		Byte type = 0;
		if (MethodType.GET.getMethod().equals(method) || MethodType.HEAD.getMethod().equals(method)
				|| MethodType.OPTIONS.getMethod().equals(method)) {
			type = 1;
		} else if (MethodType.POST.getMethod().equals(method)) {
			type = 2;
		} else if (MethodType.PUT.getMethod().equals(method) || MethodType.PATCH.getMethod().equals(method)) {
			type = 3;
		} else if (MethodType.DELETE.getMethod().equals(method)) {
			type = 4;
		}
		return type;
	}

	/**
	 * 获取请求参数 HttpServletRequest\HttpServletResponse\MultipartFile\Model
	 * 
	 * @param joinPoint
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private String getRequestParams(HttpServletRequest request) throws IOException {
		String parameterForQuery = request.getQueryString();
		String parameterForBody = "";
		if(!request.getMethod().equalsIgnoreCase(MethodType.GET.getMethod())) {
			RemoveSpaceHttpServletRequestWrapper remove = (RemoveSpaceHttpServletRequestWrapper) request;
			String body = remove.getBody();
			if(StringUtils.isNotBlank(body)) {
				Map<String, Object> parameterMap = JsonUtils.jsonToMap(body);
				parameterForBody = DictionaryOrderUtils.lowestDictionaryOrderString(parameterMap);
			}
		}
		if(StringUtils.isNotBlank(parameterForQuery) && StringUtils.isNotBlank(parameterForBody)) {
			return parameterForBody;
		}
		if(StringUtils.isBlank(parameterForQuery) && StringUtils.isBlank(parameterForBody)) {
			return "";
		}
		if(StringUtils.isNotBlank(parameterForQuery)) {
			return parameterForQuery;
		}
		return parameterForBody;
	}
	/**
	 * 获取名称
	 * 
	 * @param joinPoint
	 * @return
	 */
	private String getRequestName(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		String name = "";
		Syslog sysLog = null;
		if ((sysLog = method.getDeclaredAnnotation(Syslog.class)) != null
				&& StringUtils.isNotBlank(name = sysLog.value())) {
			return name;
		}
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = method.getName();
		name = className + "." + methodName + "()";
		return name;
	}

	/**
	 * 获取Code信息
	 * @param request
	 * @return
	 */
	private String getSysCode(HttpServletRequest request) {
		String[] uniqueIds = syslogProperties.getUniqueIds();
		for(int i=0;i<uniqueIds.length;i++) {
			String uniqueId = getOption(request, uniqueIds[i]);
			if (StringUtils.isNotBlank(uniqueId)) {
				return uniqueId;
			}
		}
		return null;
	}
	/**
	 * 获取option
	 * @param request
	 * @param optionName
	 * @return
	 */
	private static String getOption(HttpServletRequest request, String optionName) {
		String option = RequestUtils.getHeaderOptional(request, optionName, null);
		if (option == null) {
			option = RequestUtils.getCookieOptional(request, optionName, null);
		}
		return option;
	}
}
