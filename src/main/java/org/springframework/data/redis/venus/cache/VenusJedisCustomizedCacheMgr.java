package org.springframework.data.redis.venus.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.venus.consts.VenusSpringDataRedisConsts;
import org.springframework.data.redis.venus.util.VenusProcessUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class VenusJedisCustomizedCacheMgr {
	private static VenusJedisCustomizedCacheMgr instance;
	
	public static VenusJedisCustomizedCacheMgr getInstance() {
		if (instance == null) {
			synchronized (VenusJedisCustomizedCacheMgr.class) {
				if (instance == null) {
					instance = new VenusJedisCustomizedCacheMgr();
				}
			}
		}
		
		return instance;
	}
	
	private LoadingCache<String , String> slowKeysCache = CacheBuilder.newBuilder().initialCapacity(
			VenusSpringDataRedisConsts.DEFAULT_SLOWKEYS_CACHE_SIZE).maximumSize(VenusSpringDataRedisConsts.DEFAULT_MAX_SLOWKEYS_CACHE_SIZE)
			.refreshAfterWrite(VenusSpringDataRedisConsts.DEFAULT_REFREASH_INTERVAL , TimeUnit.SECONDS)
			.recordStats().build(new CacheLoader<String , String>() {
				@Override
				public String load(String key) throws Exception {
					return VenusSpringDataRedisConsts.DEFAULT_JEDIS_SLOW_KEY_LOCAL_CACHE_VALUE + VenusProcessUtils.getPid();
				}
			});
	
	public String get(String key) {
		try {
			String value = slowKeysCache.get(key);
			if ((VenusSpringDataRedisConsts.DEFAULT_JEDIS_SLOW_KEY_LOCAL_CACHE_VALUE + VenusProcessUtils.getPid()).equals(value)) {
				return null;
			}
			
			return value;
		} catch (ExecutionException e) {
			return null;
		}
	}
	
	public void put(String key , String value) {
		slowKeysCache.put(key, value);	
	}
}
