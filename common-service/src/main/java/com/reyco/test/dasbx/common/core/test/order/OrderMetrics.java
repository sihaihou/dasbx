package com.reyco.test.dasbx.common.core.test.order;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Component
public class OrderMetrics {
    
    private final MeterRegistry meterRegistry;
    private final Counter orderSubmitCounter;
    private final Counter orderProcessCounter;
    private final Counter orderMessageProcessCounter;
    private final Timer orderProcessTimer;
    
    public OrderMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        this.orderSubmitCounter = Counter.builder("order.submit.total")
                .description("订单提交总数")
                .register(meterRegistry);
                
        this.orderProcessCounter = Counter.builder("order.process.total")
                .description("订单处理总数")
                .tag("result", "success")
                .register(meterRegistry);
                
        this.orderMessageProcessCounter = Counter.builder("order.message.process.total")
                .description("订单消息处理总数")
                .register(meterRegistry);
                
        this.orderProcessTimer = Timer.builder("order.process.duration")
                .description("订单处理耗时")
                .register(meterRegistry);
    }
    
    public void recordOrderSubmit() {
        orderSubmitCounter.increment();
    }
    
    public void recordOrderProcess(long duration, boolean success) {
        orderProcessTimer.record(duration, TimeUnit.MILLISECONDS);
        
        if (success) {
            orderProcessCounter.increment();
        }
    }
    
    public void recordOrderMessageProcess(boolean success) {
        orderMessageProcessCounter.increment();
    }
}