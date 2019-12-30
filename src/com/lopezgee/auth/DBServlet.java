package com.lopezgee.auth;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.lopezgee.drivers.JsonDriverVars;
import com.lopezgee.restserver.ExtVars;
import com.lopezgee.restserver.MiniServlet;

public class DBServlet extends MiniServlet {
	
	public DBServlet (Logger log, ExtVars extvars) {
		super (log, extvars);
	}
	
	@Override
	public String[] doGet(Map<String,String> pars) {
		
		Gson json = new Gson();
		String[] ret = new String[2];
		String response = "";
		ret[0] = "application/json";
		
		//Gets initial vars for Database
		
		
		FileReader fr;

		// Loading props file
		try {
			fr = new FileReader(pars.get("ParamatersFile"));
			jvars = json.fromJson(fr, JsonDriverVars.class);
			fr.close();
		} catch (IOException e) {
			log.log(Level.SEVERE, this.getClass().getName()+"Cannot open properties file. Exiting");
			log.log(Level.SEVERE, this.getClass().getName()+Arrays.toString(e.getStackTrace()));
			System.err.println(this.getClass().getName()+"Json Driver: No se puede abrir e fichero de propiedades");
		}
		
		DataBase db = new DataBase(jvars.)
		if (pars.get("type").equals("login"))
			response = doLogin();
		
		ret[1] = json.toJson(response);
		return ret;
	}
	
	private String doLogin() {
		return null;
	}
	
	private HashMap<String, String> dbInfo(){
		Hashmap<String String> = 
	}
}
