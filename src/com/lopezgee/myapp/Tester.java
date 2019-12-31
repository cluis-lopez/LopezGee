package com.lopezgee.myapp;

import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.lopezgee.restserver.MiniServlet;

public class Tester extends MiniServlet {

	public Tester (Logger log) {
		super (log);
	}
	@Override
	public String[] doGet(Map<String,String> pars) {
		String[] ret = new String[2];
		ret[0] = "application/json";
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
