package com.reyco.dasbx.commons.utils;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Convert {
    /**
     * 源对象转目标对象
     * @param <T>
     * @param source		源对象
     * @param tragetClass	目标class
     * @return
     */
    public static <V> V sourceToTarget(Object source, Class<V> targetClass){
        JSONObject jsonObject = JsonUtils.objToJSONObject(source);
        return JsonUtils.JSONObjectToTarget(jsonObject, targetClass);
    }
    /**
     * 源对象list转目标对象list
     * @param <T>
     * @param sources
     * @param tragetClass	目标class
     * @return
     */
    public static <V, T> List<V> sourceListToTargetList(List<T> sources,Class<V> targetClass){
    	String jsonString = JSONArray.toJSONString(sources);
    	return JsonUtils.jsonListToObjList(jsonString, targetClass);
    }
    
}
