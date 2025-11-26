package com.reyco.dasbx.portal.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.reyco.dasbx.portal.service.impl.VideoServiceImpl.PlayEventNotify;

public class ApplicationLoadMonitor {
	private final List<PlayEventNotify> playEventNotifys;
	private final int maxQueueSize;
	private final AtomicLong requestCount = new AtomicLong(0);
	private final AtomicLong lastResetTime = new AtomicLong(System.currentTimeMillis());
	private static final long RESET_INTERVAL_MS = 5000; // 5秒重置

	public ApplicationLoadMonitor(List<PlayEventNotify> playEventNotifys, int maxQueueSize) {
		this.playEventNotifys = playEventNotifys;
		this.maxQueueSize = maxQueueSize;
	}

	/**
	 * 基于应用自身状态计算负载因子
	 */
	public double getSystemLoadFactor() {
		// 1. 队列使用率 (主要指标)
		double queueUsage = getQueueUsage();

		// 2. 请求速率 (辅助指标)
		double requestRate = getRequestRate();

		// 3. 线程活跃度
		double threadActivity = getThreadActivity();
		// 综合计算
		double loadFactor = queueUsage * 0.6 + requestRate * 0.3 + threadActivity * 0.1;

		return Math.max(0.0, Math.min(1.0, loadFactor));
	}

	/**
	 * 记录请求，用于计算请求速率
	 */
	public void recordRequest() {
		requestCount.incrementAndGet();

		// 定期重置计数器
		long currentTime = System.currentTimeMillis();
		long lastTime = lastResetTime.get();
		if (currentTime - lastTime > RESET_INTERVAL_MS) {
			if (lastResetTime.compareAndSet(lastTime, currentTime)) {
				requestCount.set(0);
			}
		}
	}

	private double getQueueUsage() {
		if (playEventNotifys.isEmpty()) {
			return 0.0;
		}

		int totalSize = 0;
		for (PlayEventNotify notify : playEventNotifys) {
			totalSize += notify.getBlockingQueue().size();
		}

		double averageUsage = (double) totalSize / playEventNotifys.size() / maxQueueSize;
		return Math.min(1.0, averageUsage);
	}

	private double getRequestRate() {
		long elapsed = System.currentTimeMillis() - lastResetTime.get();
		if (elapsed == 0) {
			return 0.0;
		}

		double currentRate = (double) requestCount.get() / elapsed * 1000; // 转换为每秒
		double normalizedRate = Math.min(1.0, currentRate / 1000); // 假设1000 QPS为满载

		return normalizedRate;
	}

	private double getThreadActivity() {
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
		int threadCount = threadBean.getThreadCount();
		int peakThreadCount = threadBean.getPeakThreadCount();

		// 线程数接近峰值时认为负载高
		return peakThreadCount > 0 ? Math.min(1.0, (double) threadCount / peakThreadCount) : 0.0;
	}
}
