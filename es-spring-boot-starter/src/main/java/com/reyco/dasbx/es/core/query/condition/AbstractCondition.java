package com.reyco.dasbx.es.core.query.condition;

import java.util.Collections;
import java.util.List;

public class AbstractCondition implements Condition {
	
	@Override
	public List<? extends Condition> getChildren() {
		return Collections.emptyList();
	}
	
}
