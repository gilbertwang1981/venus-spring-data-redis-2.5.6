package org.springframework.data.redis.venus.vo;

public class VenusJedisConfig {
	private Integer maxIdle;
	private Integer minIdle;
	private Integer maxTotal;
	private Long minEvictableIdleTime;
	private String host;
	private Integer port;
	private Integer connectTimeout;
	private Integer soTimeout;
	private String user;
	private String password;
	private Integer database;
	private String clientName;
	private Long maxWaitMillis;
	
	public Integer getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	public Integer getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}
	public Integer getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}
	public Long getMinEvictableIdleTime() {
		return minEvictableIdleTime;
	}
	public void setMinEvictableIdleTime(Long minEvictableIdleTime) {
		this.minEvictableIdleTime = minEvictableIdleTime;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public Integer getSoTimeout() {
		return soTimeout;
	}
	public void setSoTimeout(Integer soTimeout) {
		this.soTimeout = soTimeout;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getDatabase() {
		return database;
	}
	public void setDatabase(Integer database) {
		this.database = database;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public Long getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(Long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
}
