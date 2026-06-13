package com.reyco.dasbx.es.core.query.sort;

import org.springframework.core.Ordered;

public interface SortDefinition extends Ordered {
	@Override
	default int getOrder() {
		return 1;
	}
}
