package org.springframework.data.redis.venus.vo;

public class VenusMetrics {
	private String serviceName;
	private String host;
	private Long counter;

	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Long getCounter() {
		return counter;
	}
	public void setCounter(Long counter) {
		this.counter = counter;
	}
}
