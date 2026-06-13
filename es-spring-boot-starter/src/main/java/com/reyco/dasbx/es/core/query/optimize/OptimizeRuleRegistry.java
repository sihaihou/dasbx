package com.reyco.dasbx.es.core.query.optimize;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;

public class OptimizeRuleRegistry {
	
	private final List<OptimizeRule> optimizeRules;

	public OptimizeRuleRegistry(List<OptimizeRule> optimizeRules) {
		List<OptimizeRule> ordered = new ArrayList<>(optimizeRules);
		AnnotationAwareOrderComparator.sort(ordered);

		this.optimizeRules = ordered;
	}

	public List<OptimizeRule> getOptimizeRules() {
		return optimizeRules;
	}
	
}
