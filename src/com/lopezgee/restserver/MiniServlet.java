package com.lopezgee.restserver;

import java.util.Map;
import java.util.logging.Logger;

public class MiniServlet {
	
	protected Logger log;
	
	public MiniServlet (Logger log) {
		this.log = log;
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
}
