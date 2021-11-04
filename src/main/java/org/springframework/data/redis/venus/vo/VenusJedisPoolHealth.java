package org.springframework.data.redis.venus.vo;

import java.util.Map;

public class VenusJedisPoolHealth {
	private String localIp;
	private Integer processId;
	private String instanceKey;
	private Boolean isHealth;
	private Integer active;
	private Integer idle;
	private Integer waiter;
	private Map<String , Integer> slowKeys;
	
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	public Integer getProcessId() {
		return processId;
	}
	public void setProcessId(Integer processId) {
		this.processId = processId;
	}
	public String getInstanceKey() {
		return instanceKey;
	}
	public void setInstanceKey(String instanceKey) {
		this.instanceKey = instanceKey;
	}
	public Boolean getIsHealth() {
		return isHealth;
	}
	public void setIsHealth(Boolean isHealth) {
		this.isHealth = isHealth;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	public Integer getIdle() {
		return idle;
	}
	public void setIdle(Integer idle) {
		this.idle = idle;
	}
	public Integer getWaiter() {
		return waiter;
	}
	public void setWaiter(Integer waiter) {
		this.waiter = waiter;
	}
	public Map<String , Integer> getSlowKeys() {
		return slowKeys;
	}
	public void setSlowKeys(Map<String , Integer> slowKeys) {
		this.slowKeys = slowKeys;
	}
}
