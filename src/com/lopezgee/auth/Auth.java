package com.lopezgee.auth;

import java.util.Date;
import java.util.logging.Logger;

import com.lopezgee.drivers.DataBase;

public class Auth {
	
	DataBase db;
	Logger log;
	
	public Auth(String driverName, String props, Logger log) {
		this.log = log;
		db = new DataBase(driverName, props, log);
	}
	
	public boolean validToken(String id, String token, Logger log) {
		User u = db.getUser(id);
		boolean ret = false;
		if (u != null) {
			Date d = new Date();
			if (!u.Blocked && d.before(u.TokenValidUpTo) && u.Token.equals(token)){
				ret = true;
			}
		}
		return ret;
	}
	
	public String createUser(String name, String password, Logger log) {
		return null;
	}
	
	public String[] loginUser(String user, String passwd, Logger log) {
		return null;
	}
}
