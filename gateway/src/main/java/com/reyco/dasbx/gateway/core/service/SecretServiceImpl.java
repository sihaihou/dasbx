package com.reyco.dasbx.gateway.core.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.encrypt.AESUtils;
import com.reyco.dasbx.gateway.core.sign.api.SecretService;

@Service
public class SecretServiceImpl implements SecretService{
	
	protected static final Logger log = LoggerFactory.getLogger(SecretServiceImpl.class);
	
	@Autowired
	private SecretService secretService;
	
	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r->{
		Thread thread = new Thread(r);
		thread.setDaemon(true);
		thread.setName("com.reyco.dasbx.gateway.secretThread");
		return thread;
	});
	
	private static Map<String,String> secretMap = new HashMap<>(); 
	private final static String DEFAULT_APPID = "subixin";
	private final static String DEFAULT_SECREY = "d5a9b2c8f1e4";
	
	@PostConstruct
	public void init() {
		executor.schedule(new ResetSecretTask(), 1000*10, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public String getSecret(String appId) {
		if(appId.equalsIgnoreCase(DEFAULT_APPID)) {
			return DEFAULT_SECREY;
		}
		return secretMap.get(appId);
	}

	@Override
	public void saveSecret(String appId, String secret) {
		secretMap.put(appId, secret);
	}
	@Override
	public void clear() {
		secretMap.clear();
	}
	public class ResetSecretTask implements Runnable{
		@Override
		public void run() {
			try {
				secretService.clear();
				for(int i=0;i<20;i++) {
					String appId = RandomStringUtils.randomAlphabetic(10);
				    String secret = AESUtils.createSecret();
					secretService.saveSecret(appId, secret);
				}
			}catch(Exception e){
				log.error("###############生成密匙出错");
			}finally {
				long oneDay = 24 * 60 * 60 * 1000;
				long initDelay  = getTimeMillis("00:00:00") - System.currentTimeMillis();
				initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
				executor.schedule(new ResetSecretTask(), initDelay,TimeUnit.MILLISECONDS);
			}
		}
		/**
		 * 获取指定时间对应的毫秒数
		 * @param time "HH:mm:ss"
		 * @return
		 */
		private long getTimeMillis(String time) {
			try {
				DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
				DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
				Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
				return curDate.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
		}
	}
}
