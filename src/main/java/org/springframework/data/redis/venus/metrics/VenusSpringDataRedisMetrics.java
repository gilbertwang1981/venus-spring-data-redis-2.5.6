package org.springframework.data.redis.venus.metrics;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.data.redis.venus.consts.VenusSpringDataRedisConsts;
import org.springframework.data.redis.venus.util.VenusAddressConvertor;
import org.springframework.data.redis.venus.vo.VenusMetrics;

import com.google.gson.Gson;

public class VenusSpringDataRedisMetrics {
	private static VenusSpringDataRedisMetrics instance;
	
	private AtomicLong metricsGet = new AtomicLong(0);
	
	private String service;
	
	private Gson gson = new Gson();
	
	public static VenusSpringDataRedisMetrics getInstance() {
		if (instance == null) {
			synchronized (VenusSpringDataRedisMetrics.class) {
				if (instance == null) {
					instance = new VenusSpringDataRedisMetrics();
				}
			}
		}
		
		return instance;
	}
	
	private VenusSpringDataRedisMetrics() {
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				VenusMetrics metrics = VenusSpringDataRedisMetrics.getInstance().switchMetrics();
				if (metrics != null) {
					System.out.println("上报Metrics:" + gson.toJson(metrics));
				}
			}
		}, VenusSpringDataRedisConsts.VENUS_JEDIS_POOL_METRICS_DELAY , VenusSpringDataRedisConsts.VENUS_JEDIS_POOL_METRICS_INTERVAL);
	}
	
	public Long incrGetMetrics(String service) {
		this.service = service;
		
		return metricsGet.incrementAndGet();
	}
	
	public VenusMetrics switchMetrics() {
		if (service != null) {
			VenusMetrics metrics = new VenusMetrics();
			metrics.setCounter(metricsGet.getAndSet(0));
			metrics.setHost(VenusAddressConvertor.getLocalIPList().get(0));
			metrics.setServiceName(service);
			
			return metrics;
		} else {
			return null;
		}
	}
}
