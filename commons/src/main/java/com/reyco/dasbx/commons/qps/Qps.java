package com.reyco.dasbx.commons.qps;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;

public class Qps {
	/**
	 * QPS
	 */
	private int qps;
	/**
	 * 
	 */
	private LinkedBlockingDeque<Long> deque;

	public Qps() {
		this(1);
	}

	public Qps(int qps) {
		super();
		this.qps = qps;
		this.deque = new LinkedBlockingDeque<Long>();
	}

	/**
	 * 是否通过
	 * 
	 * @return
	 */
	public boolean pass() {
		return tryPass(new Date().getTime());
	}

	/**
	 * 尝试通过
	 * 
	 * @return
	 */
	private boolean tryPass(Long currTime) {
		if (qps > deque.size()) {
			try {
				deque.putLast(currTime);
				while (true) {
					if (!deque.isEmpty() && currTime - 1000 > deque.peekFirst()) {
						deque.pollFirst();
					} else {
						break;
					}
				}
				return true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return pass(currTime);
	}
	/**
	 * 处理通过
	 * @return
	 */
	private boolean pass(Long currTime) {
		while (true) {
			if (!deque.isEmpty() && currTime - 1000 > deque.peekFirst()) {
				deque.pollFirst();
			} else {
				if (qps > deque.size()) {
					deque.add(currTime);
					return true;
				}
				return false;
			}
		}
	}
}
