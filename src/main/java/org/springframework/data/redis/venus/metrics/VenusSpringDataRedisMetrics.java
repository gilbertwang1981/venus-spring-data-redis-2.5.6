package org.springframework.data.redis.venus.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.data.redis.venus.consts.VenusSpringDataRedisConsts;
import org.springframework.data.redis.venus.util.VenusAddressConvertor;
import org.springframework.data.redis.venus.util.VenusHttpUtils;
import org.springframework.data.redis.venus.vo.VenusMetrics;

import com.google.gson.Gson;

public class VenusSpringDataRedisMetrics {
	private static VenusSpringDataRedisMetrics instance;
	
	private Map<String , AtomicLong> metrics = new ConcurrentHashMap<>();
	
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
	
	private String getVenusMetricsReportUrl() {
		String url = System.getenv(VenusSpringDataRedisConsts.VENUS_JEDIS_METRICS_URL_PROD_EVN_VAR);
		if (url == null) {
			return VenusSpringDataRedisConsts.VENUS_JEDIS_METRICS_URL_TEST;
		} else {
			return url;
		}
	}
	
	private VenusSpringDataRedisMetrics() {
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				List<VenusMetrics> metrics = VenusSpringDataRedisMetrics.getInstance().switchMetrics();
				
				VenusHttpUtils.sendHttpPost(getVenusMetricsReportUrl(), gson.toJson(metrics));
			}
		}, VenusSpringDataRedisConsts.VENUS_JEDIS_POOL_METRICS_DELAY , VenusSpringDataRedisConsts.VENUS_JEDIS_POOL_METRICS_INTERVAL);
	}
	
	public Long incrGetMetrics(String clientName) {
		AtomicLong counter = metrics.get(clientName);
		if (counter != null) {
			return counter.incrementAndGet();
		} else {
			metrics.put(clientName , new AtomicLong(1));
			
			return 1L;
		}
	}
	
	public List<VenusMetrics> switchMetrics() {
		List<VenusMetrics> metricsList = new ArrayList<>();
		for (Map.Entry<String , AtomicLong> entry : metrics.entrySet()){
			VenusMetrics venus = new VenusMetrics();
			venus.setClientName(entry.getKey());
			venus.setCounter(entry.getValue().getAndSet(0L));
			venus.setHost(VenusAddressConvertor.getLocalIPList().get(0));
			
			metricsList.add(venus);
		}
		return metricsList;
	}
}
