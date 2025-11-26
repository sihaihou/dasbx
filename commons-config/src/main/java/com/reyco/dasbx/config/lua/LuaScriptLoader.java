package com.reyco.dasbx.config.lua;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

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
	 * 加载配置文件
	 * @author  reyco
	 * @date    2022年5月19日
	 * @version v1.0.1 
	 * @param classLoader
	 * @return
	 */
	private static Map<String, String> loadLuas(ClassLoader classLoader) {
		Map<String, String> result = cache.get(classLoader);
		if (result != null) {
			return result;
		}
		try {
			Enumeration<URL> urls = (classLoader != null ?
					classLoader.getResources(LUA_FACTORIES_RESOURCE_LOCATION) :
					ClassLoader.getSystemResources(LUA_FACTORIES_RESOURCE_LOCATION));
			result = new ConcurrentHashMap<String, String>();
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				File directory = new File(url.getFile());
				File[] luaFiles = directory.listFiles((dir, name) -> name.endsWith(LUA_FILE_EXTENSION));
				for(File file : luaFiles) {
					String fileName = file.getName();
					String luaScriptName = fileName.substring(0, fileName.lastIndexOf("."));
		            String content = new String(Files.readAllBytes(file.toPath()));
		            result.computeIfAbsent(luaScriptName,k->content);
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
