package com.reyco.dasbx.es.core.query.optimize.bool;

import java.util.ArrayList;
import java.util.List;

import com.reyco.dasbx.es.core.query.condition.BoolCondition;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.optimize.AbstractOptimizeRule;

public class BoolFlattenOptimizeRule extends AbstractOptimizeRule{
	
	protected void doApply(Condition condition) {
		if (!(condition instanceof BoolCondition)) {
			return;
		}

		BoolCondition bool = (BoolCondition) condition;
		flatten(bool.getMust());
		flatten(bool.getShould());
		flatten(bool.getFilter());
		flatten(bool.getMustNot());
	}

	private void flatten(List<Condition> conditions) {
		if (conditions == null || conditions.isEmpty()) {
			return;
		}

		List<Condition> result = new ArrayList<>();
		for (Condition condition : conditions) {
			if (condition instanceof BoolCondition) {
				BoolCondition child = (BoolCondition) condition;
				// flatten must
				if (isFlattenable(child)) {
					result.addAll(child.getMust());
					continue;
				}
			}
			result.add(condition);
		}
		conditions.clear();
		conditions.addAll(result);
	}

	private boolean isFlattenable(BoolCondition bool) {
		return empty(bool.getShould()) && empty(bool.getFilter()) && empty(bool.getMustNot());
	}

	private boolean empty(List<?> list) {
		return list == null || list.isEmpty();
	}

	@Override
	public int getOrder() {
		return 20;
	}

	
}
