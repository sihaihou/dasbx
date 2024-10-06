package com.reyco.dasbx.commons.utils.convert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonUtils {
	/**
	 * 
	 * 对象转json字符串
	 * @param t
	 * @return
	 */
	public static <V> String objToJson(V t) {
		return JSON.toJSONString(t);
	}
	/**
	 * json字符串转目标对象
	 * @param <V>
	 * @param json
	 * @param targetClazz
	 * @return
	 */
	public static <V> V jsonToObj(String json, Class<V> targetClazz) {
		V v = JSONArray.parseObject(json, targetClazz);
		return v;
	}
	/**
	 * jsonList字符串转目标对象list
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <V> List<V> jsonListToObjList(String jsonList, Class<V> targetClazz) {
		List<V> list = JSONArray.parseArray(jsonList, targetClazz);
		return list;
	}
	
	/**
	 * 对象转JSONObject
	 * @param obj
	 * @return
	 */
	public static JSONObject objToJSONObject(Object obj) {
		return (JSONObject)JSONObject.toJSON(obj);
	}
	/**
	 * JSONObject转目标对象
	 * @param jsonObject
	 * @param targetClas
	 * @return
	 */
	public static <V> V JSONObjectToTarget(JSONObject jsonObject,Class<V> tragetClass) {
		return JSONObject.toJavaObject(jsonObject, tragetClass);
	}
	
	public static Map<String,Object> jsonToMap(String json) {
		Map<String,Object> map = JSON.parseObject(json, Map.class);
		return map;
	}
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		String json = "{\r\n" + 
				"          \"birthday\" : 1390492800000,\r\n" + 
				"          \"createBy\" : 3,\r\n" + 
				"          \"email\" : \"18307200210@163.com\",\r\n" + 
				"          \"faceUri\" : \"http://www.housihai.com/upload/images/logo-1.png\",\r\n" + 
				"          \"gender\" : 1,\r\n" + 
				"          \"gmtCreate\" : 1681195940000,\r\n" + 
				"          \"gmtModified\" : 1688995922534,\r\n" + 
				"          \"id\" : 2,\r\n" + 
				"          \"integral\" : 0,\r\n" + 
				"          \"modifiedBy\" : 1,\r\n" + 
				"          \"nickname\" : \"普通用户\",\r\n" + 
				"          \"password\" : \"20f267b9acd7368b3e475ac046d394b0eaf1762c22feec1999a49d0980cafd69\",\r\n" + 
				"          \"phone\" : \"18307200210\",\r\n" + 
				"          \"primaryKeyId\" : \"2\",\r\n" + 
				"          \"remark\" : \"普通用户\",\r\n" + 
				"          \"salt\" : \"peUbAblByniKkOEAbukS\",\r\n" + 
				"          \"state\" : 1,\r\n" + 
				"          \"suggestion\" : [\r\n" + 
				"            \"普通用户\",\r\n" + 
				"            \"18307200210@163.com\",\r\n" + 
				"            \"18307200210\",\r\n" + 
				"            \"user\"\r\n" + 
				"          ]}";
		
		Map<String, Object> jsonToMap = jsonToMap(json);
		System.out.println(jsonToMap);
		
		
	}
}
