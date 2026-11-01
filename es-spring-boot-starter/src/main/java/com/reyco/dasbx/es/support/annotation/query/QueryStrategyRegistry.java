package com.reyco.dasbx.es.support.annotation.query;

import java.lang.reflect.Array;
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
				(bool, param) -> bool.filter(Querys.terms(param.field, toTermsValues(param.value))));

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
			}else if(!(value instanceof EsRangeParam)){
				throw new SearchAnnotationException("The RANGE type of @EsQuery must be annotated on EsRangeParam!");
			}else {
				EsRangeParam range = (EsRangeParam)value;
				//有效判断
				if (range.getGte() == null
				        && range.getLte() == null
				        && range.getGt() == null
				        && range.getLt() == null) {
				    throw new SearchAnnotationException(
				        "RANGE query requires at least one bound");
				}
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
				EsGeoDistanceParam center = (EsGeoDistanceParam)value;
				Double latitude = center.getLatitude();
				Double longitude = center.getLongitude();
				Double distance = center.getDistance();
				
				if (latitude == null || longitude == null || distance == null) {
				    throw new SearchAnnotationException(
				            "GEO_DISTANCE requires latitude, longitude and distance");
				}

				if (latitude < -90 || latitude > 90) {
				    throw new SearchAnnotationException(
				            "GEO_DISTANCE latitude must be between -90 and 90");
				}

				if (longitude < -180 || longitude > 180) {
				    throw new SearchAnnotationException(
				            "GEO_DISTANCE longitude must be between -180 and 180");
				}
				if (distance <= 0 || distance.isNaN() || distance.isInfinite()) {
				    throw new SearchAnnotationException(
				            "GEO_DISTANCE distance must be a positive finite number");
				}
				if (latitude.isNaN() || latitude.isInfinite()
				        || longitude.isNaN() || longitude.isInfinite()) {
				    throw new SearchAnnotationException(
				            "GEO_DISTANCE coordinates must be finite numbers");
				}

				if (center.getUnit() == null || center.getOrder() == null) {
				    throw new SearchAnnotationException(
				            "GEO_DISTANCE unit and order must not be null");
				}
				bool.filter(Querys.geoDistance(
								param.field, 
								latitude, 
								longitude,
								distance, 
								center.getUnit(), 
								center.getOrder()));
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
				EsGeoBoundingBoxParam box = (EsGeoBoundingBoxParam)value;
				Double top = box.getTop();
				Double left = box.getLeft();
				Double bottom = box.getBottom();
				Double right = box.getRight();
				
				if (top == null || left == null || bottom == null || right == null) {
				    throw new SearchAnnotationException(
				            "GEO_BOUNDING_BOX requires top, left, bottom and right");
				}

				if (top < -90 || top > 90 || bottom < -90 || bottom > 90) {
				    throw new SearchAnnotationException(
				            "GEO_BOUNDING_BOX latitude must be between -90 and 90");
				}

				if (left < -180 || left > 180 || right < -180 || right > 180) {
				    throw new SearchAnnotationException(
				            "GEO_BOUNDING_BOX longitude must be between -180 and 180");
				}
				if (top <= bottom) {
				    throw new SearchAnnotationException(
				            "GEO_BOUNDING_BOX top must be greater than bottom");
				}
				if (top.isNaN() || top.isInfinite()
				        || left.isNaN() || left.isInfinite()
				        || bottom.isNaN() || bottom.isInfinite()
				        || right.isNaN() || right.isInfinite()) {
				    throw new SearchAnnotationException(
				            "GEO_BOUNDING_BOX coordinates must be finite numbers");
				}
				bool.filter(Querys.geoBoundingBox(
				        param.field, top, left, bottom, right));
			}
		});

		// 6. GEO_POLYGON 多边形围栏查询
		STRATEGY_MAP.put(EsQueryType.GEO_POLYGON, (bool, param) -> {
			Object value = param.value;
			if(value==null) {
				throw new SearchAnnotationException("The List<EsGeoPolygonParam> class that must be annotated for the GEO_POLYGON type of @EsQuery cannot be empty!");
			}else if(!(value instanceof List<?>)){
				throw new SearchAnnotationException("The GEO_POLYGON type of @EsQuery must be annotated on List<EsGeoPolygonParam>!");
			}else {
				List<?> rawPoints = (List<?>) value;
			    if (rawPoints.size() < 3) {
			        throw new SearchAnnotationException(
			                "GEO_POLYGON query requires at least 3 points");
			    }
			    // 由于 Dto 里声明的就是 List<EsGeoPolygonParam>，这里直接安全强转
			    List<EsGeoPolygonParam> points = new ArrayList<>(rawPoints.size());
			    for (Object item : rawPoints) {
			        if (!(item instanceof EsGeoPolygonParam)) {
			            throw new SearchAnnotationException(
			                    "GEO_POLYGON list contains an invalid point type");
			        }
			        points.add((EsGeoPolygonParam) item);
			    }
			    
			    List<GeoPoint> geoPoints = new ArrayList<>(points.size());
			    for (EsGeoPolygonParam point : points) {
			        Double latitude = point.getLatitude();
			        Double longitude = point.getLongitude();

			        if (latitude == null || longitude == null) {
			            throw new SearchAnnotationException(
			                    "GEO_POLYGON point latitude and longitude must not be null");
			        }

			        if (latitude < -90 || latitude > 90) {
			            throw new SearchAnnotationException(
			                    "GEO_POLYGON latitude must be between -90 and 90");
			        }
			        if (longitude < -180 || longitude > 180) {
			            throw new SearchAnnotationException(
			                    "GEO_POLYGON longitude must be between -180 and 180");
			        }
			        geoPoints.add(new GeoPoint(latitude, longitude));
				}
			    bool.filter(Querys.geoPolygon(param.field, geoPoints));
			}
		});
	}

	public void apply(EsQueryType type, BoolCondition bool, QueryParam queryParam) {
		BiConsumer<BoolCondition, QueryParam> handler = STRATEGY_MAP.get(type);
        if (handler != null) {
            handler.accept(bool, new QueryParam(queryParam.field, queryParam.value, queryParam.annotation));
        }
	}

	
	
	private static Collection<?> toTermsValues(Object value) {
	    if (value instanceof Collection) {
	        return (Collection<?>) value;
	    }

	    if (value != null && value.getClass().isArray()) {
	        int length = Array.getLength(value);
	        List<Object> values = new ArrayList<>(length);
	        for (int i = 0; i < length; i++) {
	            values.add(Array.get(value, i));
	        }
	        return values;
	    }
	    throw new SearchAnnotationException(
	            "TERMS query must be annotated on a Collection or array field");
	}
}
