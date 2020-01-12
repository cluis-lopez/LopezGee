package com.lopezgee.myapp;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.lopezgee.auth.AuthServer;
import com.lopezgee.auth.DataBase;
import com.lopezgee.auth.User;
import com.lopezgee.drivers.JsonDriverVars;
import com.lopezgee.restserver.MiniServlet;

public class DBServlet extends MiniServlet {

	@Override
	public String[] doGet(Map<String, String> pars) {

		String[] ret = new String[2];

		// Open the Database props file

		FileReader fr;
		JsonDriverVars jvars = null;
		Gson json = new Gson();

		try {
			fr = new FileReader(pars.get("ParamatersFile"));
			jvars = json.fromJson(fr, JsonDriverVars.class);
			fr.close();
		} catch (IOException e) {
			log.log(Level.SEVERE, "Cannot open properties file. Exiting");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			System.err.println("Json Driver: No se puede abrir el fichero de propiedades");
		}
		// Gets initial vars for Database

		if (jvars != null && pars.get("adminuser").equals(jvars.Admin)
				&& pars.get("adminpassword").equals(jvars.Password)) {
			DataBase db = new DataBase(pars.get("DatabaseDriver"), pars.get("ParamatersFile"), log);

			if (pars.get("type").equals("login"))
				ret = doLogin(jvars, pars.get("adminuser"), pars.get("adminpassword"));

			if (pars.get("type").equals("info"))
				ret = doInfo(db);

			if (pars.get("type").equals("newUser"))
				ret = doNewUser(db, pars.get("id"), pars.get("name"), pars.get("password"));

			if (pars.get("type").equals("removeUser"))
				ret = doRemoveUser(db, pars.get("removeid"));
			
		} else if (jvars != null && pars.get("type").equals("login")) {
			ret = doLogin(jvars, pars.get("adminuser"), pars.get("adminpassword"));
		} else {
			ret[0] = "text/plain";
			ret[1] = "Unauthorized";
		}

		return ret;
	}

	private String[] doLogin(JsonDriverVars j, String user, String password) {
		String[] ret = new String[2];
		ret[0] = "text/plain";
		ret[1] = "Unauthorized";
		if (user.equals(j.Admin) && password.equals(j.Password)) {
			ret[1] = "OK";
		}
		return ret;
	}

	private String[] doInfo(DataBase db) {
		String[] ret = new String[2];
		Gson json = new Gson();
		ret[0] = "application/json";
		ret[1] = json.toJson(db.getInfo());
		return ret;

	}

	private String[] doNewUser(DataBase db, String id, String name, String password) {
		String[] ret = new String[2];
		ret[0] = "text/plain";
		User u = new User(id, name, password);
		ret[1] = db.createUser(u);
		String temp = db.update();
		if(!temp.equals("OK"))
			ret[1] = "Cannot create user "+id;
		return ret;
	}

	private String[] doRemoveUser(DataBase db, String id) {
		String[] ret = new String[2];
		ret[0] = "text/plain";
		User u = db.getUser(id);
		ret[1] = db.deleteUser(u);
		String temp = db.update();
		if(!temp.equals("OK"))
			ret[1] = "Cannot remove user "+id;
		return ret;
	}

}
