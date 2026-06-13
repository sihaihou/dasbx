package com.reyco.dasbx.es.support.annotation.sort;

public enum EsSortType {
	
	/** 1. 普通字段排序 */
    FIELD,
    /** 2. 评分得分排序 */
    SCORE,
    /** 3. 地理位置距离排序 */
    GEO_DISTANCE,
    /** 4. 动态脚本排序 */
    SCRIPT,
    
}
