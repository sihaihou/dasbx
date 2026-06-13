package com.reyco.dasbx.es.support.annotation.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import com.reyco.dasbx.es.core.query.condition.BoolCondition;
import com.reyco.dasbx.es.core.query.condition.Querys;
import com.reyco.dasbx.es.core.query.condition.RangeCondition;
import com.reyco.dasbx.es.core.query.geo.GeoPoint;
import com.reyco.dasbx.es.support.annotation.execptio.SearchAnnotationException;

public class QueryStrategyRegistry {

	// 1. 策略路由表：每种查询类型对应一个特定的 Querys 组装逻辑
	private static final Map<EsQueryType, BiConsumer<BoolCondition, QueryParam>> STRATEGY_MAP = new HashMap<>();

	static {
		// 精确匹配
		STRATEGY_MAP.put(EsQueryType.TERM, (bool, param) -> bool.filter(Querys.term(param.field, param.value)));

		// IN 查询 (集合/数组)
		STRATEGY_MAP.put(EsQueryType.TERMS,
				(bool, param) -> bool.filter(Querys.terms(param.field, (Collection<?>) param.value)));

		// 单字段分词匹配
		STRATEGY_MAP.put(EsQueryType.MATCH, (bool, param) -> bool.must(Querys.match(param.field, param.value)));

		// 多字段分词匹配 (优先读取注解里配置的 multiFields)
		STRATEGY_MAP.put(EsQueryType.MULTI_MATCH, (bool, param) -> {
			String[] fields = param.annotation.multiFields().length > 0 ? param.annotation.multiFields()
					: new String[] { param.field };
			bool.must(Querys.multiMatch(param.value, fields));
		});

		// 范围查询range
		STRATEGY_MAP.put(EsQueryType.RANGE, (bool, param) -> {
			Object value = param.value;
			if(value==null) {
				throw new SearchAnnotationException("The EsRangeParam class that must be annotated for the RANGE type of @EsQuery cannot be empty!");
			}else if(!(value instanceof EsGeoDistanceParam)){
				throw new SearchAnnotationException("The RANGE type of @EsQuery must be annotated on EsRangeParam!");
			}else {
				if (value instanceof EsRangeParam) {
					EsRangeParam range = (EsRangeParam)value;
	
					// 动态构建单个 range 节点的复合条件
					RangeCondition rangeCondition = Querys.range(param.field);
					if (range.getGte() != null) {
						rangeCondition.gte(range.getGte());
					}
					if (range.getLte() != null) {
						rangeCondition.lte(range.getLte());
					}
					if (range.getGt() != null) {
						rangeCondition.gt(range.getGt());
					}
					if (range.getLt() != null) {
						rangeCondition.lt(range.getLt());
					}
					bool.filter(rangeCondition);
				}
			}
		});

		// 通配符模糊查询
		STRATEGY_MAP.put(EsQueryType.WILD_CARD,
				(bool, param) -> bool.filter(Querys.wildcard(param.field, "*" + param.value + "*")));

		// PREFIX 前缀查询
		STRATEGY_MAP.put(EsQueryType.PREFIX,
				(bool, param) -> bool.filter(Querys.prefix(param.field, param.value.toString())));

		// REGEXP 正则查询
		STRATEGY_MAP.put(EsQueryType.REGEXP,
				(bool, param) -> bool.filter(Querys.regexp(param.field, param.value.toString())));

		// FUZZY 模糊纠错查询
		STRATEGY_MAP.put(EsQueryType.FUZZY,
				(bool, param) -> bool.must(Querys.fuzzy(param.field, param.value.toString())));

		// GEO_DISTANCE 地理距离查询
		STRATEGY_MAP.put(EsQueryType.GEO_DISTANCE, (bool, param) -> {
			Object value = param.value;
			if(value==null) {
				throw new SearchAnnotationException("The EsGeoDistanceParam class that must be annotated for the GEO_DISTANCE type of @EsQuery cannot be empty!");
			}else if(!(value instanceof EsGeoDistanceParam)){
				throw new SearchAnnotationException("The GEO_DISTANCE type of @EsQuery must be annotated on EsGeoDistanceParam!");
			}else {
				if (value instanceof EsGeoDistanceParam) {
					EsGeoDistanceParam center = (EsGeoDistanceParam)value;
					if (center.getLatitude() != null && center.getLongitude() != null && center.getDistance() != null
							&& center.getUnit() != null && center.getOrder() != null) {
						bool.filter(Querys
								.geoDistance(param.field, center.getLatitude(), center.getLongitude(),
										center.getDistance(), 
										center.getUnit(), 
										center.getOrder()));
					}
				}
			}
		});

		// 5. GEO_BOUNDING_BOX 矩形边界查询
		STRATEGY_MAP.put(EsQueryType.GEO_BOUNDING_BOX, (bool, param) -> {
			Object value = param.value;
			if(value==null) {
				throw new SearchAnnotationException("The EsGeoBoundingBoxParam class that must be annotated for the GEO_BOUNDING_BOX type of @EsQuery cannot be empty!");
			}else if(!(value instanceof EsGeoBoundingBoxParam)){
				throw new SearchAnnotationException("The GEO_BOUNDING_BOX type of @EsQuery must be annotated on EsGeoBoundingBoxParam!");
			}else {
				if (value instanceof EsGeoBoundingBoxParam) {
					EsGeoBoundingBoxParam box = (EsGeoBoundingBoxParam)value;
					if (box.getTop() != null && box.getLeft() != null && box.getBottom() != null
							&& box.getRight() != null) {
						// 完美匹配你的底层方法签名！
						bool.filter(Querys.geoBoundingBox(param.field, box.getTop(), box.getLeft(), box.getBottom(),
								box.getRight()));
					}
				}
			}
		});

		// 6. GEO_POLYGON 多边形围栏查询
		STRATEGY_MAP.put(EsQueryType.GEO_POLYGON, (bool, param) -> {
			Object value = param.value;
			if(value==null) {
				throw new SearchAnnotationException("The List<EsGeoPolygonParam> class that must be annotated for the GEO_POLYGON type of @EsQuery cannot be empty!");
			}else if(!(value instanceof EsGeoBoundingBoxParam)){
				throw new SearchAnnotationException("The GEO_POLYGON type of @EsQuery must be annotated on List<EsGeoPolygonParam>!");
			}else {
				if (value instanceof List) {
					// 由于 Dto 里声明的就是 List<EsGeoPolygonParam>，这里直接安全强转
					@SuppressWarnings("unchecked")
					List<EsGeoPolygonParam> points = (List<EsGeoPolygonParam>)value;
					if (!points.isEmpty()) {
						List<GeoPoint> geoPoints = new ArrayList<>();
						GeoPoint geoPoint = null;
						for (EsGeoPolygonParam esGeoPolygonParam : points) {
							geoPoint = new GeoPoint();
							geoPoint.setLatitude(esGeoPolygonParam.getLatitude());
							geoPoint.setLongitude(esGeoPolygonParam.getLongitude());
							geoPoints.add(geoPoint);
						}
						bool.filter(Querys.geoPolygon(param.field, geoPoints));
					}
				}
			}
		});
	}

	public void apply(EsQueryType type, BoolCondition bool, QueryParam queryParam) {
		BiConsumer<BoolCondition, QueryParam> handler = STRATEGY_MAP.get(type);
        if (handler != null) {
            handler.accept(bool, new QueryParam(queryParam.field, queryParam.value, queryParam.annotation));
        }
	}

}
