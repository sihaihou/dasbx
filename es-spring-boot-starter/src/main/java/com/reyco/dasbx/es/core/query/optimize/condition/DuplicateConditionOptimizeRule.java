package com.reyco.dasbx.es.core.query.optimize.condition;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.reyco.dasbx.es.core.query.condition.BoolCondition;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.optimize.AbstractOptimizeRule;

public class DuplicateConditionOptimizeRule extends AbstractOptimizeRule {

	@Override
	protected void doApply(Condition condition) {
		if (!(condition instanceof BoolCondition)) {
			return;
		}

		BoolCondition bool = (BoolCondition) condition;
		deduplicate(bool.getMust());
		deduplicate(bool.getFilter());
	}

	private void deduplicate(List<Condition> conditions) {
		if (conditions == null || conditions.isEmpty()) {
			return;
		}
		Set<String> exists = new HashSet<>();
		Iterator<Condition> iterator = conditions.iterator();
		while (iterator.hasNext()) {
			Condition condition = iterator.next();
			String key = buildKey(condition);
			if (!exists.add(key)) {
				iterator.remove();
			}
		}
	}

	private String buildKey(Condition condition) {
		return condition.toString();
	}

	@Override
	public int getOrder() {
		return 40;
	}
}
