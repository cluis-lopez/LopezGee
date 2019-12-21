package com.clopez.restserver;

import java.util.Map;

import com.google.gson.Gson;

public class Tester extends MiniServlet {

	public Tester() {
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
