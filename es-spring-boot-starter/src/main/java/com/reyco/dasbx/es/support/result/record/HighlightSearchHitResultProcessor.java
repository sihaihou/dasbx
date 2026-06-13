package com.reyco.dasbx.es.support.result.record;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.springframework.util.StringUtils;

import com.reyco.dasbx.es.core.result.SearchHitResult;

public class HighlightSearchHitResultProcessor implements SearchHitResultProcessor {

	// 结构化元数据缓存：Map<Class, List<HighlightFieldMeta>> -> 一次扫描，终身享用
	private static final Map<Class<?>, List<HighlightFieldMeta>> META_CACHE = new ConcurrentHashMap<>();
	// 属性读取器缓存：Map<Class, Map<字段名, 该类对应的Lambda读取器>>
	private static final Map<Class<?>, Map<String, Function<Object, Object>>> GETTER_CACHE = new ConcurrentHashMap<>();

	@Override
	public <T> void process(T entity, SearchHitResult<T> hitResult) {
		if (entity == null) {
			return;
		}
		
		Class<?> clazz = entity.getClass();

		// 1. 缓存该类所有普通字段的 Getter 供读取原值使用（冷启动）
		Map<String, Function<Object, Object>> allGetters = GETTER_CACHE.computeIfAbsent(clazz, c -> {
			Map<String, Function<Object, Object>> map = new ConcurrentHashMap<>();
			for (Field field : c.getDeclaredFields()) {
				Function<Object, Object> getter = createGetter(c, field.getName());
				if (getter != null) {
					map.put(field.getName(), getter);
				}
			}
			return map;
		});

		// 2. 一键扫描并缓存当前类的【高亮配对元数据大盘】（冷启动）
		List<HighlightFieldMeta> metas = META_CACHE.computeIfAbsent(clazz, c -> {
			List<HighlightFieldMeta> list = new ArrayList<>();
			for (Field field : c.getDeclaredFields()) {
				// 核心：只看打了高亮注解的属性（如 highlightUsername）
				if (field.isAnnotationPresent(EsHighlightResult.class)) {
					EsHighlightResult annotation = field.getAnnotation(EsHighlightResult.class);

					// 🎯 黄金纽带：注解里的 value 明确指出了它的原字段是谁（如 "username"）
					String originalFieldName = StringUtils.hasText(annotation.value()) ? annotation.value()
							: field.getName();
					String highlightFieldName = field.getName(); // 当前高亮字段名（如 "highlightUsername"）

					BiConsumer<Object, Object> setter = createSetter(c, highlightFieldName);
					if (setter != null) {
						list.add(new HighlightFieldMeta(originalFieldName, setter));
					}
				}
			}
			return list;
		});

		// 3. 第一步：全自动【无条件原值迁移保底】
		// 在看 ES 结果之前，先把所有高亮字段全部塞入它们各自对应的原属性值。这样即使后面没命中高亮，高亮字段也有干净的原值托底！
		for (HighlightFieldMeta meta : metas) {
			Function<Object, Object> originalGetter = allGetters.get(meta.esKey);
			if (originalGetter != null) {
				Object originalValue = originalGetter.apply(entity);
				if (originalValue != null) {
					meta.highlightSetter.accept(entity, originalValue.toString()); // 纯 Lambda 级快速保底注入
				}
			}
		}
		Map<String, List<String>> highlights = hitResult.getHighlights();
		// 4. 第二步：【动态高亮红字精准覆盖】
		// 如果 ES 确实返回了高亮，则用高亮红字把刚才的保底值无情覆盖掉！
		if (highlights == null || highlights.isEmpty()) {
			return;
		}

		for (Entry<String, List<String>> entry : highlights.entrySet()) {
			String esKey = entry.getKey(); // ES 原生字段名，如 "username"
			List<String> value = entry.getValue();
			if (value == null || value.isEmpty()) {
				continue;
			}

			String highlightContent = value.get(0);

			// 5. 从大盘里精确匹配 ES 字段名对应的元数据
			for (HighlightFieldMeta meta : metas) {
				if (meta.esKey.equals(esKey)) {
					// 有且仅有改写打了注解的高亮字段，绝对不碰、也不降级去改写原字段！
					meta.highlightSetter.accept(entity, highlightContent);
				}
			}
		}
	}

	// ================================================================
	// LambdaMetafactory 核心魔法：高性能运行时轻量代理组装
	// ================================================================

	private static BiConsumer<Object, Object> createSetter(Class<?> clazz, String fieldName) {
		try {
			String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			MethodHandle targetMethod = null;
			for (Method method : clazz.getMethods()) {
				if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
					targetMethod = MethodHandles.lookup().unreflect(method);
					break;
				}
			}
			if (targetMethod == null)
				return null;
			CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "accept",
					MethodType.methodType(BiConsumer.class),
					MethodType.methodType(void.class, Object.class, Object.class), targetMethod, targetMethod.type());
			return (BiConsumer<Object, Object>) callSite.getTarget().invokeExact();
		} catch (Throwable e) {
			return null;
		}
	}

	private static Function<Object, Object> createGetter(Class<?> clazz, String fieldName) {
		try {
			String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			MethodHandle targetMethod = null;
			for (Method method : clazz.getMethods()) {
				if ((method.getName().equals(getterName) || method.getName()
						.equals("is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)))
						&& method.getParameterCount() == 0) {
					targetMethod = MethodHandles.lookup().unreflect(method);
					break;
				}
			}
			if (targetMethod == null)
				return null;
			CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "apply",
					MethodType.methodType(Function.class), MethodType.methodType(Object.class, Object.class),
					targetMethod, targetMethod.type());
			return (Function<Object, Object>) callSite.getTarget().invokeExact();
		} catch (Throwable e) {
			return null;
		}
	}
	// ================================================================
	// 高内聚的配对元数据实体
	// ================================================================
	private static class HighlightFieldMeta {
		/** ES 中的原始字段名（注解的 value），同时也是配对的原属性名，如 "username" */
		final String esKey;
		/** 高亮字段的设置器（如 highlightUsername 的 Setter） */
		final BiConsumer<Object, Object> highlightSetter;

		HighlightFieldMeta(String esKey, BiConsumer<Object, Object> highlightSetter) {
			this.esKey = esKey;
			this.highlightSetter = highlightSetter;
		}
	}
}
