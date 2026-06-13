package com.reyco.dasbx.es.core.query.optimize.nested;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.reyco.dasbx.es.core.query.condition.BoolCondition;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.condition.NestedCondition;
import com.reyco.dasbx.es.core.query.optimize.AbstractOptimizeRule;

public class NestedMergeOptimizeRule extends AbstractOptimizeRule {

	@Override
	protected void doApply(Condition condition) {
		if (!(condition instanceof BoolCondition)) {
			return;
		}

		BoolCondition bool = (BoolCondition) condition;
		merge(bool.getMust());
		merge(bool.getFilter());
		merge(bool.getShould());
		merge(bool.getMustNot());
	}

	private void merge(List<Condition> conditions) {
		if (conditions == null || conditions.isEmpty()) {
			return;
		}

		Map<String, NestedCondition> nestedMap = new LinkedHashMap<>();
		List<Condition> result = new ArrayList<>();
		for (Condition condition : conditions) {
			if (!(condition instanceof NestedCondition)) {
				result.add(condition);
				continue;
			}

			NestedCondition nested = (NestedCondition) condition;
			String path = nested.getPath();
			if (path == null) {
				result.add(condition);
				continue;
			}

			NestedCondition exists = nestedMap.get(path);
			if (exists == null) {
				nestedMap.put(path, nested);
				result.add(nested);
				continue;
			}
			mergeNested(exists, nested);
		}
		
		conditions.clear();
		conditions.addAll(result);
	}

	private void mergeNested(NestedCondition target, NestedCondition source) {
		Condition targetCondition = target.getCondition();
		Condition sourceCondition = source.getCondition();

		//target already bool
		if (targetCondition instanceof BoolCondition) {
			BoolCondition bool = (BoolCondition) targetCondition;
			bool.getMust().add(sourceCondition);
			return;
		}

		//create bool
		BoolCondition bool = new BoolCondition();
		bool.getMust().add(targetCondition);
		bool.getMust().add(sourceCondition);
		target.setCondition(bool);
	}

	@Override
	public int getOrder() {
		return 50;
	}
}
