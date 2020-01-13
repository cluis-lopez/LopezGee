package com.lopezgee.auth;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

public class AuthServer {
	
	DataBase db;
	Logger log;
	
	public AuthServer(String driverName, String props, Logger log) {
		this.log = log;
		db = new DataBase(driverName, props, log);
	}
	
	public boolean isValidToken(String id, String token) {
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
	
	public String[] loginUser(String id, String passwd) {
		String [] ret = new String[2];
		User u = db.getUser(id);
		if (u == null) {
			ret[0] = "FAIL"; ret [1] = "Invalid User";
		} else if (Encrypt.checkPasswd(passwd, u.Password, u.Salt)) { // Valid User+Password
			ret[0] = "OK";
			// Check if there's already a valid token
			if (!u.Token.equals("") && u.TokenValidUpTo.after(new Date())) {
				ret[1] = u.Token;
			} else {
			//Generate a new Token
			UUID uuid = UUID.randomUUID();
			db.updateToken(u, uuid.toString());
			db.update();
			ret[1]=uuid.toString();
			}
		} else {
			ret[0]="FAIL"; ret[1]="Invalid Password";
		}
		return ret;
	}
}
