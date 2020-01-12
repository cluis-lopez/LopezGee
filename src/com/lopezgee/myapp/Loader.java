package com.lopezgee.myapp;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.lopezgee.restserver.MiniServlet;

public class Loader extends MiniServlet {
	@Override
	public String[] doGet(Map<String, String> pars) {
		String[] ret = new String[2];
		Map<String,String> map = new HashMap<>();
		Gson json = new Gson();
		ret[0]="application/json";
		
		//Load the server just doing ....
		
		ret[1] = json.toJson(map);
		return ret;
	}
}
