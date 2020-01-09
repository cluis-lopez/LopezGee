package com.lopezgee.restserver;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MiniServlet {
	
	protected Logger log;
	protected Map<String, Long> timers;
	protected long tinit;
	
	public MiniServlet (Logger log) {
		this.log = log;
		timers = new HashMap<>();
		tinit = System.nanoTime();
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
		timers.put("main", System.nanoTime()-tinit);
		return timers;
	}
}
