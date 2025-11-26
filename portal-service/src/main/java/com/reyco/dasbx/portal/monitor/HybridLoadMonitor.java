package com.reyco.dasbx.portal.monitor;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.portal.service.impl.VideoServiceImpl.PlayEventNotify;

public class HybridLoadMonitor {
	private final SystemLoadMonitor systemMonitor;
	private final ApplicationLoadMonitor appMonitor;
	private final AtomicLong lastLogTime = new AtomicLong(0);

	public HybridLoadMonitor(List<PlayEventNotify> playEventNotifys, int maxQueueSize) {
		this.systemMonitor = new SystemLoadMonitor();
		this.appMonitor = new ApplicationLoadMonitor(playEventNotifys, maxQueueSize);
	}

	/**
	 * 混合负载因子：结合系统和应用指标
	 */
	public double getSystemLoadFactor() {
		double systemLoad = systemMonitor.getSystemLoadFactor();
		double appLoad = appMonitor.getSystemLoadFactor();

		// 系统负载权重随应用负载增加而增加
		// 当应用负载低时，主要看系统负载
		// 当应用负载高时，系统负载权重增加
		double systemWeight = 0.3 + appLoad * 0.4; // 30% - 70%
		double appWeight = 1.0 - systemWeight;
		double hybridLoad = systemLoad * systemWeight + appLoad * appWeight;

		// 记录监控日志
		logMonitorData(systemLoad, appLoad, hybridLoad);

		return hybridLoad;
	}

	public void recordRequest() {
		appMonitor.recordRequest();
	}
	private void logMonitorData(double systemLoad, double appLoad, double hybridLoad) {
        long currentTime = System.currentTimeMillis();
        long lastTime = lastLogTime.get();
        
        // 每30秒记录一次详细监控数据
        if (currentTime - lastTime > 30000) {
            if (lastLogTime.compareAndSet(lastTime, currentTime)) {
            	Logger logger = LoggerFactory.getLogger("SystemLoadMonitor");
                logger.info("负载监控 - 系统: {:.2f}, 应用: {:.2f}, 混合: {:.2f}", 
                    systemLoad, appLoad, hybridLoad);
            }
        }
    }
}
