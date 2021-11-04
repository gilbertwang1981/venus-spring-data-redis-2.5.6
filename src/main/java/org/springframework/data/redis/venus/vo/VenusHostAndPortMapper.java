package org.springframework.data.redis.venus.vo;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.HostAndPortMapper;

public class VenusHostAndPortMapper implements HostAndPortMapper {
	
	private HostAndPort hap;
	
	public VenusHostAndPortMapper(String host , Integer port) {
		this.setHap(new HostAndPort(host , port));
	}

	@Override
	public HostAndPort getHostAndPort(HostAndPort hap) {
		return hap;
	}

	public HostAndPort getHap() {
		return hap;
	}

	public void setHap(HostAndPort hap) {
		this.hap = hap;
	}
}
