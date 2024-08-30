package com.reyco.dasbx.es.core.search;

import java.util.List;

public class SimpleSortSearchDto extends SimpleSearchDto implements SortSearchDto{
	private List<Sort> sorts;
	@Override
	public List<Sort> getSorts() {
		return this.sorts;
	}
	public void setSorts(List<Sort> sorts) {
		this.sorts = sorts;
	}
}
