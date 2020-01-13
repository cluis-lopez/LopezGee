package com.lopezgee.myapp;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.lopezgee.restserver.MiniServlet;

public class Login extends MiniServlet {
	
	@Override
	public String[] doGet(Map<String, String> pars) {
		String[] ret = new String[2];
		Map<String,String> map = new HashMap<>();
		Gson json = new Gson();
		ret[0]="application/json";
		String temp[] = authServer.loginUser(pars.get("id"), pars.get("password"));
		
		if (temp[0].equals("OK")) {
			map.put("id",  pars.get("id")); map.put("token", temp[1]);
		} else {
			map.put("id",  temp[0]); map.put("token", temp[1]);
		}
		
		ret[1] = json.toJson(map);
		return ret;
	}
}
