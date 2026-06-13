package com.reyco.dasbx.es.core.query.optimize;

import com.reyco.dasbx.es.core.query.condition.Condition;

/**
 * QueryOptimizer.apply()
 * 			↓
 * Condition.traverse()
 * 			↓
 * EmptyBoolOptimizer.doApply()
 */
public abstract class AbstractOptimizeRule implements OptimizeRule {

	@Override
	public void apply(Condition root) {
		root.traverse(this::doApply);
	}
	
	protected abstract void doApply(Condition condition);
	
}
