package org.springframework.data.redis.venus.checker;

import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.data.redis.venus.consts.VenusSpringDataRedisConsts;
import org.springframework.data.redis.venus.keys.VenusSlowKeys;
import org.springframework.data.redis.venus.util.VenusAddressConvertor;
import org.springframework.data.redis.venus.util.VenusHttpUtils;
import org.springframework.data.redis.venus.util.VenusProcessUtils;
import org.springframework.data.redis.venus.vo.VenusJedisPoolHealth;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class VenusJedisPoolChecker {
	private String instanceKey;
	private JedisPool jedisPool;
	private String clientName;
	
	private Gson gson = new Gson();
	
	public VenusJedisPoolChecker(String clientName) {
		this.setClientName(clientName);
	}
	
	private String getVenusHealthCheckUrl() {
		String url = System.getenv(VenusSpringDataRedisConsts.VENUS_JEDIS_CTRL_URL_PROD_HC_EVN_VAR);
		if (url == null) {
			return VenusSpringDataRedisConsts.VENUS_JEDIS_CTRL_URL_TEST_HC;
		} else {
			return url;
		}
	}
	
	public void start() {
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				VenusJedisPoolHealth hc = new VenusJedisPoolHealth();
				hc.setInstanceKey(instanceKey);
				hc.setLocalIp(VenusAddressConvertor.getLocalIPList().get(0));
				hc.setProcessId(VenusProcessUtils.getPid());
				
				Map<String , Integer> slowKeys = VenusSlowKeys.getInstance().getBigKeys(clientName);
				if (slowKeys != null) {
					hc.setSlowKeys(slowKeys);
				} else {
					hc.setSlowKeys(Collections.emptyMap());
				}
				
				Jedis jedis = null;
				try {
					jedis = jedisPool.getResource();
					String resp = jedis.ping();
					if ("PONG".equalsIgnoreCase(resp)) {
						hc.setIsHealth(true);
					} else {
						hc.setIsHealth(false);
					}
					hc.setActive(jedisPool.getNumActive());
					hc.setIdle(jedisPool.getNumIdle());
					hc.setWaiter(jedisPool.getNumWaiters());
				} catch (Exception ex) {
					hc.setIsHealth(false);
					hc.setActive(-1);
					hc.setIdle(-1);
					hc.setWaiter(-1);
				} finally {
					if (jedis != null) {
						jedis.close();
					}
				}
				
				VenusHttpUtils.sendHttpPost(getVenusHealthCheckUrl() , gson.toJson(hc));
			}
			
		}, VenusSpringDataRedisConsts.VENUS_JEDIS_POOL_HC_DELAY , VenusSpringDataRedisConsts.VENUS_JEDIS_POOL_HC_INTERVAL);
	}


	public String getInstanceKey() {
		return instanceKey;
	}


	public void setInstanceKey(String instanceKey) {
		this.instanceKey = instanceKey;
	}


	public JedisPool getJedisPool() {
		return jedisPool;
	}


	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}
