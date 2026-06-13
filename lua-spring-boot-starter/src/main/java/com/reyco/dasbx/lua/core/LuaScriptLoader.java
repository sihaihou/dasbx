package com.reyco.dasbx.lua.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

public class LuaScriptLoader {
	
	private static Logger log = LoggerFactory.getLogger(LuaScriptLoader.class);
	 /**
     * Lua文件在classpath中的位置
     */
    public static final String LUA_FACTORIES_RESOURCE_LOCATION = "LUA/";
    /**
     * Lua文件扩展名
     */
    private static final String LUA_FILE_EXTENSION = ".lua";
    
    private static final Map<ClassLoader, Map<String, String>> cache = new ConcurrentHashMap<>();
	
	public static String loadLuas(String scriptName,ClassLoader classLoader) {
		Assert.notNull(scriptName, "'factoryClass' must not be null");
		ClassLoader classLoaderToUse = classLoader;
		if (classLoaderToUse == null) {
			classLoaderToUse = LuaScriptLoader.class.getClassLoader();
		}
		String luaScript = loadLuaScript(scriptName, classLoaderToUse);
		if (luaScript == null) {
			throw new IllegalArgumentException("Lua script not found: " + scriptName);
		}
		return luaScript;
	}
	/**
	 * @author  reyco
	 * @date    2022年5月19日
	 * @version v1.0.1 
	 * @param factoryClass
	 * @param classLoader
	 * @return
	 */
	private static String loadLuaScript(String scriptName,ClassLoader classLoader) {
		return loadLuas(classLoader).getOrDefault(scriptName,null);
	}
	/**
	 * 加载配置文件（完美兼容本地IDE与Jar包环境）
	 * @author  reyco
	 * @date    2026年6月13日（优化版）
	 * @version v1.0.2 
	 * @param classLoader
	 * @return
	 */
	private static Map<String, String> loadLuas(ClassLoader classLoader) {
	    Map<String, String> result = cache.get(classLoader);
	    if (result != null) {
	        return result;
	    }
	    try {
	        result = new ConcurrentHashMap<String, String>();
	        // 1. 创建 Spring 的资源模式解析器
	        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
	        
	        // 2. 使用通配符扫描所有的类路径（包括当前项目和依赖的Jar包）
	        // 这里的表达式相当于把以前的目录遍历，变成了直接由 Spring 帮你把匹配的文件扣出来
	        String locationPattern = "classpath*:" + LUA_FACTORIES_RESOURCE_LOCATION + "**/*" + LUA_FILE_EXTENSION;
	        Resource[] resources = resolver.getResources(locationPattern);
	        
	        if (resources != null) {
	            for (Resource resource : resources) {
	                // 3. 获取文件名（例如: SlidingWindowRateLimit.lua）
	                String fileName = resource.getFilename();
	                if (fileName == null || !fileName.endsWith(LUA_FILE_EXTENSION)) {
	                    continue;
	                }
	                
	                // 4. 提取脚本名称作为 Map 的 key
	                String luaScriptName = fileName.substring(0, fileName.lastIndexOf("."));
	                
	                // 5. 跨平台安全地读取脚本内容（不再依赖物理路径，而是直接走输入流）
	                String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
	                
	                result.computeIfAbsent(luaScriptName, k -> content);
	            }
	        }
	        cache.put(classLoader, result);
	        return result;
	    }
	    catch (IOException ex) {
	        throw new IllegalArgumentException("Unable to load lua from location [" +
	                LUA_FACTORIES_RESOURCE_LOCATION + "]", ex);
	    }
	}
	
	/**
	 * 获取全部lua脚本
	 * @return
	 */
	public static Set<String> getLoadedScriptNames() {
		if(cache.isEmpty()) {
			loadLuas(LuaScriptLoader.class.getClassLoader());
		}
		Set<String> result = new HashSet<>();
		for(Entry<ClassLoader, Map<String, String>> entry : cache.entrySet()) {
			for(String luaName : entry.getValue().keySet()) {
				result.add(luaName);
			}
		}
		return result;
	}
	
}
