package com.reyco.dasbx.lua.core;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuaScriptFactory {
	
	private static Logger log = LoggerFactory.getLogger(LuaScriptLoader.class);
    
    /**
     * EVALSHA
     * @param scriptName
     * @return
     */
	public static String getLuaScript(String scriptName){
		return LuaScriptLoader.loadLuas(scriptName, LuaScriptFactory.class.getClassLoader());
	}
	/**
	 * 
	 * @param scriptName
	 * @return
	 */
	public static Set<String> getLuaScripts(){
		return LuaScriptLoader.getLoadedScriptNames();
	}
}
