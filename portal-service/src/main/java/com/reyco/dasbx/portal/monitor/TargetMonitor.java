package com.reyco.dasbx.portal.monitor;

import java.util.concurrent.ArrayBlockingQueue;

public class TargetMonitor {
	private ArrayBlockingQueue<Object> blockingQueue;

	public ArrayBlockingQueue<Object> getBlockingQueue() {
		return blockingQueue;
	}

	public void setBlockingQueue(ArrayBlockingQueue<Object> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}
}
