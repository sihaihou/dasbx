package com.reyco.dasbx.es.core.query.condition;

import java.util.Collections;
import java.util.List;

/**
 * 嵌套查询
 * 
 * @author reyco
 *
 */
public class NestedCondition extends AbstractCondition {

	private String path;

	private Condition condition;

	public NestedCondition(String path, Condition condition) {
		this.path = path;
		this.condition = condition;
	}

	@Override
	public List<Condition> getChildren() {
		if (condition == null) {
			return Collections.emptyList();
		}
		return Collections.singletonList(condition);
	}

	public String getPath() {
		return path;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	public Condition getCondition() {
		return condition;
	}
}
