package org.springframework.data.redis.venus.keys;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.venus.consts.VenusSpringDataRedisConsts;

public class VenusSlowKeys {
	private ConcurrentHashMap<String , Map<String , Integer>> clients = new ConcurrentHashMap<>();
	
	private static VenusSlowKeys instance;
	
	public static VenusSlowKeys getInstance() {
		if (instance == null) {
			synchronized (VenusSlowKeys.class) {
				if (instance == null) {
					instance = new VenusSlowKeys();
				}
			}
		}
		
		return instance;
	}
	
	public Map<String , Integer> getBigKeys(String clientName) {
		return clients.get(clientName);
	}
	
	public void checkIfBigKeys(String clientName , String key , Integer size) {
		try {
			if (size >= VenusSpringDataRedisConsts.DEFAULT_REDIS_BIG_KEY_SIZE) {
				clients.get(clientName).putIfAbsent(key, size);
			} else {
				clients.get(clientName).remove(key);
			}
		} catch (Exception e) {
		}
	}
	
	public void registerClient(String clientName) {
		clients.put(clientName , new ConcurrentHashMap<String , Integer>());
	}
}
