package com.lopezgee.restserver;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.lopezgee.auth.AuthServer;

public class MiniServlet {
	
	protected Logger log;
	protected Map<String, Long> servletTimers;
	protected long rtinit, cpuinit;
	protected AuthServer authServer;
	protected ThreadMXBean threadMXBean;
	
	public void initialize (Logger log, AuthServer auths) {
		this.log = log;
		this.authServer = auths;
		servletTimers = new HashMap<>();
		threadMXBean = ManagementFactory.getThreadMXBean();
		threadMXBean.setThreadContentionMonitoringEnabled(true);
		rtinit = System.nanoTime();
		cpuinit = threadMXBean.getCurrentThreadCpuTime();
	}
	
	public String[] doGet(Map<String, String> map) {
		// This method should be overrided
		String[] ret = new String[2];
		ret[0] = "text/plain";
		ret[1] = "This method should be overrided";
		return ret;
	};
	
	public String[] doPost(Map<String, String> map) {
		// This method should be overrided
		String[] ret = new String[2];
		ret[0] = "text/plain";
		ret[1] = "This method should be overrided";
		return ret;
	};
	
	public final Map<String, Long> destroy() {
		servletTimers.put("mainRT", System.nanoTime() - rtinit);
		servletTimers.put("mainCPU", threadMXBean.getCurrentThreadCpuTime() - cpuinit);
		return servletTimers;
	}
}
