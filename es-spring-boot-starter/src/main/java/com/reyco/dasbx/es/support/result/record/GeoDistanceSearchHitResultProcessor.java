package com.reyco.dasbx.es.support.result.record;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.annotation.AnnotatedElementUtils;

import com.reyco.dasbx.es.core.result.SearchHitResult;

public class GeoDistanceSearchHitResultProcessor implements SearchHitResultProcessor {
	
	private final Map<Class<?>, Field> cache = new ConcurrentHashMap<>();
	
	@Override
	public <T> void process(T entity, SearchHitResult<T> hitResult) {
		if (hitResult.getDistance() == null) {
            return;
        }
		Field field = getDistanceField(entity.getClass());
		if (field == null) {
            return;
        }
		try {
			field.set(entity, hitResult.getDistance());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Field getDistanceField(Class<?> clazz) {
		return cache.computeIfAbsent(clazz, this::resolveDistanceField);
	}

	private Field resolveDistanceField(Class<?> clazz) {
		Class<?> current = clazz;
		while (current != null && current != Object.class) {
			for (Field field : current.getDeclaredFields()) {
				EsGeoDistanceResult annotation = AnnotatedElementUtils.findMergedAnnotation(field,
						EsGeoDistanceResult.class);
				if (annotation != null) {
					field.setAccessible(true);
					return field;
				}
			}
			current = current.getSuperclass();
		}
		return null;
	}
}
