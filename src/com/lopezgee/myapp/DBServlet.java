package com.lopezgee.myapp;

import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.lopezgee.auth.DataBase;
import com.lopezgee.restserver.MiniServlet;

public class DBServlet extends MiniServlet {
	
	public DBServlet (Logger log) {
		super (log);
	}
	
	@Override
	public String[] doGet(Map<String,String> pars) {
		
		String[] ret = new String[2];
		
		//Gets initial vars for Database
		
		DataBase db = new DataBase(pars.get("DatabaseDriver"), pars.get("ParamatersFile"), log);
		
		if (pars.get("type").equals("info"))
				ret = doInfo(db);
		
		return ret;
	}
	
	private String doLogin() {
		return null;
	}
	
	private String[] doInfo(DataBase db) {
		String[] ret = new String[2];
		Gson json= new Gson();
		ret[0] = "application/json";
		ret[1] = json.toJson(db.getInfo());
		return ret;
		
	}
	
}
