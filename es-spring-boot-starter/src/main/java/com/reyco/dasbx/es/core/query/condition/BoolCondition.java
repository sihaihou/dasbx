package com.reyco.dasbx.es.core.query.condition;

import java.util.ArrayList;
import java.util.List;

public class BoolCondition extends AbstractCondition {

	private List<Condition> must = new ArrayList<>();

    private List<Condition> should = new ArrayList<>();

    private List<Condition> filter = new ArrayList<>();

    private List<Condition> mustNot = new ArrayList<>();
    
    @Override
	public List<Condition> getChildren() {
		List<Condition> children = new ArrayList<>();
		children.addAll(must);
		children.addAll(should);
		children.addAll(filter);
		children.addAll(mustNot);

		return children;
	}
	public BoolCondition must(Condition condition){
        must.add(condition);
        return this;
    }
	public BoolCondition should(Condition condition){
		should.add(condition);
        return this;
    }
	public BoolCondition filter(Condition condition){
        filter.add(condition);
        return this;
    }
	public BoolCondition mustNot(Condition condition){
		mustNot.add(condition);
        return this;
    }
	
	public List<Condition> getMust() {
		return must;
	}
	public List<Condition> getShould() {
		return should;
	}
	public List<Condition> getFilter() {
		return filter;
	}
	public List<Condition> getMustNot() {
		return mustNot;
	}
}