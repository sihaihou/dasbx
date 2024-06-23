package com.reyco.dasbx.gateway.core.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.reyco.dasbx.gateway.core.constant.Constants;
import com.reyco.dasbx.gateway.core.utils.RequestUtils;

/**
 * 静态排除忽略资源
 * @author reyco
 *
 */
public class DefaultStaticExcludeIgnoringResources implements StaticExclusionIgnoringResources{
	
	protected PathMatcher pathMatcher = new AntPathMatcher();
	/**
	 * 排除请求key：service(服务) value：servicePath服务地址
	 */
	private Map<String,Set<String>> excludeMap;
	
	private Map<String,Boolean> cacheExcludeMap = new HashMap<String,Boolean>();
	/**
	 * 静态资源/排除请求
	 * @param request 请求对象
	 * @return
	 */
	protected Boolean isStaticResourceOrExclude(ServerHttpRequest request) {
		String path = RequestUtils.getPath(request);
		//静态资源/是否排除请求
		if(cacheExcludeMap.containsKey(path)) {
			if(cacheExcludeMap.get(path)) {
				return true;
			}else {
				return false;
			}
		}
		if(isStaticResource(request) || isExcludeResource(request)) {
			cacheExcludeMap.put(path,true);
			return true;
		}
		cacheExcludeMap.put(path, false);
		return false;
	}
	/**
     * 根据请求路径判断是否为静态资源请求
     * @param request 请求对象
     */
	@Override
	public boolean isStaticResource(ServerHttpRequest request) {
		String path = RequestUtils.getPath(request);
    	String staticResourcePath = path;
        Pattern pattern = Pattern.compile(Constants.STATIC_RESOURCE_PATTERN);
        Matcher matcher = pattern.matcher(staticResourcePath);
        return matcher.find();
	}
	/**
	 * 是否排除请求
	 * @param request
	 * @return
	 */
	@Override
	public boolean isExcludeResource(ServerHttpRequest request) {
		String path = request.getPath().toString();
		String originalPath = path;
		String[] originalPathArray = originalPath.split("\\/");
		String service = originalPathArray[1];
		Set<String> servicePathSet = excludeMap.get(service);
		if(servicePathSet!=null) {
			for(String tempPath:servicePathSet) {
				if(pathMatcher.match(tempPath, originalPath)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 忽略内部访问
	 * @param request
	 * @return
	 */
	@Override
	public Boolean ignoreInner(ServerHttpRequest request) {
		String dasbxAccept = RequestUtils.getHeader(request, Constants.ACCEPT_NAME);
	   	if(dasbxAccept.equalsIgnoreCase("true")) {
	   		return true;
	   	}
	   	return false;
	}
	public void setExclude(Set<String> exclude) {
		if(excludeMap==null) {
			excludeMap = new HashMap<String,Set<String>>();
		}
		exclude.stream().forEach(path->{
			String[] pathArray = path.split("\\/");
			String service = pathArray[1];
			Set<String> servicePathSet = excludeMap.get(service);
			if(servicePathSet==null) {
				servicePathSet = new HashSet<String>();
				excludeMap.put(service, servicePathSet);
			}
			servicePathSet.add(path);
		});
	}
	public Map<String, Set<String>> getExcludeMap() {
		return excludeMap;
	}
	public void setExcludeMap(Map<String, Set<String>> excludeMap) {
		this.excludeMap = excludeMap;
	}
}
