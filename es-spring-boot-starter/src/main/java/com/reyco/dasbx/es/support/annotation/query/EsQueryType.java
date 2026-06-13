package com.reyco.dasbx.es.support.annotation.query;

public enum EsQueryType {
	
	/**
	 * 精确匹配
	 * <p>使用示例：</p>
	 * <pre>{@code
     * @EsQuery(type = EsQueryType.TERM,field="name")
     * private String keywork;
     * </pre>
	 */
    TERM,
    
    /**
     * 数组/集合多值匹配 (IN)
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.TERMS,field="name")
     * private List<String> names;
     * </pre>
     */
    TERMS,
    
    /**
     * 单字段全文检索
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.MATCH,field="name")
     * private String keywork;
     * </pre>
     */
    MATCH,
    
    /**
     * 多字段全文检索
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.MULTI_MATCH, multiFields = {"name","desc","remark"})
     * private String keywork;
     * </pre>
     */
    MULTI_MATCH,
    
    /**
     * 范围查询：必须使用 {@code com.reyco.dasbx.es.support.annotation.query.EsRangeParam}对象.
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.RANGE, field = "createTime")
     * private EsRangeParam rangeParam;
     * </pre>
     */
    RANGE,
    
    /**
     * 模糊通配符 (*val*)
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.WILD_CARD,field="name")
     * private String wildCard;
     * </pre>
     */
    WILD_CARD,
    
    /**
     * 前缀查询 (prefix)
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.PREFIX,field="name")
     * private String prefix;
     * </pre>
     */
    PREFIX,
    
    /**
     * 正则查询 (regexp)
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.REGEXP,field="name")
     * private String regexp;
     * </pre>
     */
    REGEXP,
    
    /**
     * 模糊/纠错查询 (fuzzy)
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.FUZZY,field="name")
     * private String fuzzy;
     * </pre>
     */
    FUZZY,
    
    /**
     * 地理距离（附近的人/车）：必须使用{@code com.reyco.dasbx.es.support.annotation.query.EsGeoDistanceParam}
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.GEO_DISTANCE,field="location")
     * private EsGeoDistanceParam geoDistance;
     * </pre>
     */
    GEO_DISTANCE,
    
    /**
     * 地理矩形边界（地图视野内),必须使用{@code com.reyco.dasbx.es.support.annotation.query.EsGeoBoundingBoxParam}
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.GEO_BOUNDING_BOX,field="location")
     * private EsGeoBoundingBoxParam geoBoundingBox;
     * </pre>
     */
    GEO_BOUNDING_BOX,
    
    /**
     * 地理多边形（自定义电子围栏）,必须使用{@code com.reyco.dasbx.es.support.annotation.query.EsGeoPolygonParam}
     * <p>使用示例：</p>
     * <pre>{@code
     * @EsQuery(type = EsQueryType.GEO_POLYGON,field="location")
     * private EsGeoPolygonParam EsGeoPolygonParam;
     * </pre>
     */
    GEO_POLYGON         
}