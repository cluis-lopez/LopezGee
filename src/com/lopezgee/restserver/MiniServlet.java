package com.lopezgee.restserver;

import java.util.Map;
import java.util.logging.Logger;

public class MiniServlet {
	
	Logger log;
	ExtVars extvars;
	
	public MiniServlet (Logger log, ExtVars extvars) {
		this.log = log;
		this.extvars = extvars;
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
