package com.lopezgee.auth;

import java.util.HashMap;

public interface DataBaseIF {
	public User getUser(String id);
	public String createUser(User u);
	public String deleteUser(User u);
	public String updateToken(User u, String newToken);
	public String close();
	public HashMap<String, String> getInfo();
}