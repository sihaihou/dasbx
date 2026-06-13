package com.reyco.dasbx.es.core.query.optimize.bool;

import java.util.List;

import com.reyco.dasbx.es.core.query.condition.BoolCondition;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.optimize.AbstractOptimizeRule;

public class EmptyBoolOptimizeRule extends AbstractOptimizeRule {
	
	@Override
	protected void doApply(Condition condition) {
		if (!(condition instanceof BoolCondition)) {
			return;
		}

		BoolCondition bool = (BoolCondition) condition;
		removeEmpty(bool.getMust());
		removeEmpty(bool.getShould());
		removeEmpty(bool.getFilter());
		removeEmpty(bool.getMustNot());
	}

	private void removeEmpty(List<Condition> conditions) {
		if (conditions == null) {
			return;
		}
		conditions.removeIf(this::isEmptyBool);
	}

	private boolean isEmptyBool(Condition condition) {
		if (!(condition instanceof BoolCondition)) {
			return false;
		}
		BoolCondition bool = (BoolCondition) condition;
		return bool.getChildren().isEmpty();
	}

	@Override
	public int getOrder() {
		return 10;
	}
}
