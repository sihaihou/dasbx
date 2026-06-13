package com.reyco.dasbx.es.core.query.optimize.bool;

import java.util.List;

import com.reyco.dasbx.es.core.query.condition.BoolCondition;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.optimize.AbstractOptimizeRule;

public class SingleBoolOptimizeRule extends AbstractOptimizeRule {

	@Override
	protected void doApply(Condition condition) {
		if (!(condition instanceof BoolCondition)) {
			return;
		}
		BoolCondition bool = (BoolCondition) condition;
		simplify(bool.getMust());
		simplify(bool.getFilter());
	}

	private void simplify(List<Condition> conditions) {
		if (conditions == null || conditions.size() != 1) {
			return;
		}
		Condition child = conditions.get(0);
		if (!(child instanceof BoolCondition)) {
			return;
		}
		BoolCondition nested = (BoolCondition) child;
		if (!nested.getShould().isEmpty()) {
			return;
		}
		conditions.clear();
		conditions.addAll(nested.getMust());
	}

	@Override
	public int getOrder() {
		return 30;
	}
}
