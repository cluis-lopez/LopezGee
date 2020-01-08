package com.lopezgee.auth;

import java.util.HashMap;

public interface DataBaseIF {
	public String update();
	public String close();
	public String deleteDatabase();
	public User getUser(String id);
	public String createUser(User u);
	public String deleteUser(User u);
	public String updateToken(User u, String newToken);
	public HashMap<String, String> getInfo();
}