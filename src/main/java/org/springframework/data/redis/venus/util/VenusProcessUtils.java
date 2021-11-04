package org.springframework.data.redis.venus.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class VenusProcessUtils {
	public static int getPid() {
	    RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
	    String name = runtime.getName(); 
	    try {
	        return Integer.parseInt(name.substring(0, name.indexOf('@')));
	    } catch (Exception e) {
	        return -1;
	    }
	}
}
