package com.reyco.dasbx.es.core.query.optimize;

import org.springframework.core.Ordered;

import com.reyco.dasbx.es.core.query.condition.Condition;

public interface OptimizeRule extends Ordered {

	void apply(Condition root);

}