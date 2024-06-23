package com.reyco.dasbx.config.utils;

import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.CodePrefixType;

public class CodeUtils {
	
	private static Logger logger = LoggerFactory.getLogger(CodeUtils.class);
	
	/**
	 * 生成角色人物编号
	 * @Version 0.1
	 **/
	public static String getPersonageCode() {
		return getCode(CodePrefixType.PERSONAGE);
	}
	/**
	 * 生成区域人物编号
	 * @Version 0.1
	 **/
	public static String getAreaCode() {
		return getCode(CodePrefixType.AREA);
	}
	/**
	 * 获取Code
	 * @return
	 */
	public static String getCode(CodePrefixType codePrefixType){
		Long maxNumber = getMaxNumber(codePrefixType.getPrefix());
		String poCode = buildSequenceCode(codePrefixType.getPrefix(), maxNumber, codePrefixType.getLength());
		logger.info("生成"+codePrefixType.getPrefix()+"编号 : {}", poCode);
		return poCode;
	}
	private static Long getMaxNumber(String key) {
		StringRedisTemplate stringRedisTemplate = SpringContextUtils.getBean(StringRedisTemplate.class);
		return stringRedisTemplate.opsForValue().increment(CachePrefixConstants.SEQUENCE_CODE_PREFIX+key);
	}
	/**
	 * 获取序列code
	 * @param prefix  前缀
	 * @param incr    
	 * @param length  
	 * @return
	 */
	public static String buildSequenceCode(String prefix, Long incr, int length) {
		String incrStr = incr.toString();
		StringJoiner stringJoiner = new StringJoiner("",prefix,"");
		int len = length - incrStr.length();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				stringJoiner.add("0");
			}
		}
		stringJoiner.add(incrStr);
		return stringJoiner.toString();
	}
}
