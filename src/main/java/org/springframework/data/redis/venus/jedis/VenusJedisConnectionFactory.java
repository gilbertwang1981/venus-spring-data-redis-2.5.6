package org.springframework.data.redis.venus.jedis;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.venus.checker.VenusJedisPoolChecker;
import org.springframework.data.redis.venus.consts.VenusSpringDataRedisConsts;
import org.springframework.data.redis.venus.keys.VenusSlowKeys;
import org.springframework.data.redis.venus.util.VenusHttpUtils;
import org.springframework.data.redis.venus.vo.VenusHostAndPortMapper;
import org.springframework.data.redis.venus.vo.VenusJedisConfig;

import com.google.gson.Gson;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class VenusJedisConnectionFactory extends JedisConnectionFactory {

	private String instanceKey;
	private VenusJedisPoolChecker venusJedisPoolChecker;
	
	private Gson gson = new Gson();
	
	private Boolean isEnableVenusRedis() {
		
		String strSW = System.getenv(VenusSpringDataRedisConsts.DISABLE_VENUS_SPRING_DATA_REDIS_SW);
		if (strSW != null && Integer.parseInt(strSW) == 1) {
			return false;
		} else {
			return true;
		}
	}
	
	private JedisPool createPool(VenusJedisConfig config) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setTestOnBorrow(false);
		poolConfig.setTestOnCreate(false);
		poolConfig.setTestOnReturn(false);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setBlockWhenExhausted(false);
		
		poolConfig.setMaxIdle(config.getMaxIdle());
		poolConfig.setMaxTotal(config.getMaxTotal());
		poolConfig.setMaxWaitMillis(config.getMaxWaitMillis());
		poolConfig.setMinIdle(config.getMinIdle());
		poolConfig.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTime());
		
		return new JedisPool(poolConfig , new HostAndPort(config.getHost() , config.getPort()) , this.clientConfig);
	}
	
	private String getVenusCfgUrl() {
		String url = System.getenv(VenusSpringDataRedisConsts.VENUS_JEDIS_CTRL_URL_PROD_CFG_EVN_VAR);
		if (url == null) {
			return VenusSpringDataRedisConsts.VENUS_JEDIS_CTRL_URL_TEST_CFG;
		} else {
			return url;
		}
	}
	
	private VenusJedisConfig getVenusJedisConfigFromRemote(String instanceKey) {
		String response = VenusHttpUtils.sendHttpGet(getVenusCfgUrl() , "instanceKey=" + instanceKey);
		VenusJedisConfig config = gson.fromJson(response , VenusJedisConfig.class);
		
		return config;
	}
	
	private JedisClientConfig createClientConfig(VenusJedisConfig config) {
		DefaultJedisClientConfig.Builder builder = DefaultJedisClientConfig.builder();

		builder.connectionTimeoutMillis(config.getConnectTimeout());
		builder.socketTimeoutMillis(config.getSoTimeout());

		builder.clientName(config.getClientName());
		builder.hostAndPortMapper(new VenusHostAndPortMapper(config.getHost() , config.getPort()));
		builder.password(config.getPassword());
		builder.ssl(false);

		return builder.build();
	}
	
	@Override
	public void afterPropertiesSet() {
		if (isEnableVenusRedis()) {
			
			VenusJedisConfig config = getVenusJedisConfigFromRemote(instanceKey);
			
			VenusSlowKeys.getInstance().registerClient(config.getClientName());
			
			this.clientConfig = createClientConfig(config);
			
			this.pool = createPool(config);
			
			this.initialized = true;
			
			venusJedisPoolChecker = new VenusJedisPoolChecker(config.getClientName());
			venusJedisPoolChecker.setInstanceKey(instanceKey);
			venusJedisPoolChecker.setJedisPool((JedisPool)this.pool);
			venusJedisPoolChecker.start();
		} else  {
			super.afterPropertiesSet();
		}
	}
	
	public VenusJedisConnectionFactory(String instanceKey) {
		this.clientConfiguration = new MutableJedisClientConfiguration();
		
		this.instanceKey = instanceKey;
	}
}
