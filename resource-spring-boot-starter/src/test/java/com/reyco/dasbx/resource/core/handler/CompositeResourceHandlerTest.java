package com.reyco.dasbx.resource.core.handler;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.reyco.dasbx.resource.core.model.DefaultResourceDefinition;
import com.reyco.dasbx.resource.core.model.ResourceDefinition;

public class CompositeResourceHandlerTest {

	@Test
	public void invokesEveryHandler() {
		AtomicInteger firstCount = new AtomicInteger();
		AtomicInteger secondCount = new AtomicInteger();

		CompositeResourceHandler composite = new CompositeResourceHandler(
				Arrays.<ResourceHandler>asList(
						definition -> firstCount.incrementAndGet(),
						definition -> secondCount.incrementAndGet()));

		composite.handler(new DefaultResourceDefinition());

		assertEquals(1, firstCount.get());
		assertEquals(1, secondCount.get());
	}

	@Test
	public void continuesAfterHandlerFailure() {
		AtomicInteger successCount = new AtomicInteger();

		ResourceHandler failing = definition -> {
			throw new IllegalStateException("handler failed");
		};
		ResourceHandler successful = definition -> successCount.incrementAndGet();

		CompositeResourceHandler composite = new CompositeResourceHandler(
				Arrays.asList(failing, successful));

		composite.handler(new DefaultResourceDefinition());

		assertEquals(1, successCount.get());
	}

	@Test
	public void ignoresNullHandler() {
		AtomicInteger count = new AtomicInteger();

		CompositeResourceHandler composite = new CompositeResourceHandler(
				Arrays.asList(null, definition -> count.incrementAndGet()));

		composite.handler(new DefaultResourceDefinition());

		assertEquals(1, count.get());
	}

	@Test
	public void acceptsEmptyHandlerList() {
		CompositeResourceHandler composite = new CompositeResourceHandler(
				Collections.<ResourceHandler>emptyList());
		ResourceDefinition definition = new DefaultResourceDefinition();

		composite.handler(definition);
	}
}
