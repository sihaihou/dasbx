package com.reyco.dasbx.portal.monitor;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class SystemLoadMonitor {
	
	private final OperatingSystemMXBean osBean;
	private final Runtime runtime;
	private final AtomicLong lastUpdateTime = new AtomicLong(0);
	private final AtomicReference<Double> cachedLoadFactor = new AtomicReference<>(0.0);
	private static final long CACHE_DURATION_MS = 1000; // 缓存1秒

	public SystemLoadMonitor() {
		this.osBean = ManagementFactory.getOperatingSystemMXBean();
		this.runtime = Runtime.getRuntime();
	}

	/**
	 * 获取系统负载因子 (0.0 - 1.0) 0.0表示空闲，1.0表示满载
	 */
	public double getSystemLoadFactor() {
		long currentTime = System.currentTimeMillis();
		long lastTime = lastUpdateTime.get();

		// 使用缓存，避免频繁计算
		if (currentTime - lastTime < CACHE_DURATION_MS) {
			return cachedLoadFactor.get();
		}
		if (lastUpdateTime.compareAndSet(lastTime, currentTime)) {
			double loadFactor = calculateLoadFactor();
			cachedLoadFactor.set(loadFactor);
			return loadFactor;
		}

		return cachedLoadFactor.get();
	}

	private double calculateLoadFactor() {
		List<Double> factors = new ArrayList<>();

		// 1. 系统CPU负载 (权重: 40%)
		double cpuLoad = getCpuLoad();
		factors.add(cpuLoad * 0.4);

		// 2. 内存使用率 (权重: 30%)
		double memoryUsage = getMemoryUsage();
		factors.add(memoryUsage * 0.3);

		// 3. JVM GC压力 (权重: 20%)
		double gcPressure = getGcPressure();
		factors.add(gcPressure * 0.2);

		// 4. 系统负载平均值 (权重: 10%)
		double systemLoad = getSystemLoadAverage();
		factors.add(systemLoad * 0.1);

		// 计算加权平均值
		double totalLoad = factors.stream().mapToDouble(Double::doubleValue).sum();

		// 限制在0.0-1.0之间
		return Math.max(0.0, Math.min(1.0, totalLoad));
	}

	private double getCpuLoad() {
		try {
			if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = 
                    (com.sun.management.OperatingSystemMXBean) osBean;
                double cpuUsage = sunOsBean.getSystemCpuLoad();
                return Double.isNaN(cpuUsage) ? 0.0 : cpuUsage;
            }
		} catch (Exception e) {
			// 降级处理
		}
		return 0.0;
	}

	private double getMemoryUsage() {
		long maxMemory = runtime.maxMemory();
		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		long usedMemory = totalMemory - freeMemory;

		return (double) usedMemory / maxMemory;
	}

	private double getGcPressure() {
		List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
		long totalGcCount = 0;
		long totalGcTime = 0;

		for (GarbageCollectorMXBean gcBean : gcBeans) {
			totalGcCount += gcBean.getCollectionCount();
			totalGcTime += gcBean.getCollectionTime();
		}

		// 基于GC时间和频率计算压力
		// 假设：如果GC时间占总运行时间的比例高，说明压力大
		long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
		double gcTimeRatio = uptime > 0 ? (double) totalGcTime / uptime : 0.0;
		return Math.min(1.0, gcTimeRatio * 10); // 放大效应，便于观察
	}

	private double getSystemLoadAverage() {
		double loadAverage = osBean.getSystemLoadAverage();
		if (loadAverage < 0) {
			return 0.0; // 不可用
		}

		int availableProcessors = osBean.getAvailableProcessors();
		// 负载平均值除以CPU核心数，得到相对负载
		return Math.min(1.0, loadAverage / availableProcessors);
	}
}
