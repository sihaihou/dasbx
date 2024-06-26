package com.reyco.dasbx.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPoolConfig {

	@Bean
	public ThreadPoolExecutor threadPoolExecutor() {
		return new ThreadPoolExecutor(8, 16, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100),new ThreadFactory() {
			LongAdder count = new LongAdder();
			@Override
			public Thread newThread(Runnable r) {
				count.increment();
				Thread thread = new Thread(r);
				thread.setName("com.reyco.dasbx.threadPool-"+count.intValue());
				thread.setDaemon(false);
				return thread;
			}
		},new AbortPolicy()) ;
	}
}
