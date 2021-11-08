package org.springframework.data.redis.venus.consts;

public interface VenusSpringDataRedisConsts {
	public static final String VENUS_JEDIS_CTRL_URL_TEST_CFG = "http://test-mw-governance-service.int.chuxingyouhui.com/config/redis";
	public static final String VENUS_JEDIS_CTRL_URL_PROD_CFG_EVN_VAR = "VENUS_JEDIS_CTRL_URL_PROD_CFG_EVN_VAR";
	
	public static final Long VENUS_JEDIS_POOL_HC_DELAY = 1000L;
	public static final Long VENUS_JEDIS_POOL_HC_INTERVAL = 5000L;
	
	public static final Long VENUS_JEDIS_POOL_METRICS_DELAY = 15000L;
	public static final Long VENUS_JEDIS_POOL_METRICS_INTERVAL = 15000L;
	
	public static final String VENUS_JEDIS_CTRL_URL_TEST_HC = "http://test-mw-governance-service.int.chuxingyouhui.com/config/redis/report";
	public static final String VENUS_JEDIS_CTRL_URL_PROD_HC_EVN_VAR = "VENUS_JEDIS_CTRL_URL_PROD_HC_EVN_VAR";
	
	public static final String VENUS_JEDIS_METRICS_URL_TEST = "http://test-mw-governance-service.int.chuxingyouhui.com/config/redis/metrics";
	public static final String VENUS_JEDIS_METRICS_URL_PROD_EVN_VAR = "VENUS_JEDIS_METRICS_URL_PROD_EVN_VAR";
	
	public static final String DISABLE_VENUS_SPRING_DATA_REDIS_SW = "DISABLE_VENUS_SPRING_DATA_REDIS";
	
	public static final Long DEFAULT_REDIS_BIG_KEY_SIZE = 5 * 1024 * 1024L;
	
	public static final Integer DEFAULT_SLOWKEYS_CACHE_SIZE = 128;
	public static final Integer DEFAULT_MAX_SLOWKEYS_CACHE_SIZE = 1024;
	public static final Integer DEFAULT_REFREASH_INTERVAL = 15;
	
	public static final String DEFAULT_JEDIS_SLOW_KEY_LOCAL_CACHE_VALUE = "DEFAULT-JEDIS-SLOW-KEY-LOCAL-CACHE-VALUE-";
}
