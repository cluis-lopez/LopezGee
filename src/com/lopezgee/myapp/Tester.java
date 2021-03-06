package com.lopezgee.myapp;

import java.util.Map;
import com.google.gson.Gson;
import com.lopezgee.restserver.MiniServlet;

public class Tester extends MiniServlet {

	@Override
	public String[] doGet(Map<String,String> pars) {
		String[] ret = new String[2];
		ret[0] = "application/json";
		pars.put("timer", Long.toString((System.nanoTime()-servletTimers.get("main"))));
		Gson json = new Gson();
		ret[1] = json.toJson(pars);
		return ret;
	}

	@Override
	public String[] doPost(Map<String,String> pars) {
		String[] ret = new String[2];
		ret[0] = "application/json";
		Gson json = new Gson();
		ret[1] = json.toJson(pars);
		return ret;
	}
}
