package com.reyco.dasbx.es.core.query.condition;

import java.util.List;
import java.util.function.Consumer;

public interface Condition {
	/**
	 * children
	 */
	List<? extends Condition> getChildren();
	
	/**
	 * 遍历
	 * @param consumer
	 */
	default void traverse(Consumer<Condition> consumer) {
		consumer.accept(this);
		List<? extends Condition> children = getChildren();
		if (children == null) {
			return;
		}
		for (Condition child : children) {
			child.traverse(consumer);
		}
	}

	default boolean isValid() {
		return true;
	}

}
