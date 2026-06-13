package com.reyco.dasbx.es.core.query.condition;

import java.util.List;

/**
 * ID批量查询
 * 
 * @author reyco
 *
 */
public class IdsCondition extends AbstractCondition {

	private List<String> ids;
	
	@Override
	public boolean isValid() {
	    return ids != null && !ids.isEmpty();
	}
	
	public IdsCondition(List<String> ids) {
		this.ids = ids;
	}

	public List<String> getIds() {
		return ids;
	}
}
