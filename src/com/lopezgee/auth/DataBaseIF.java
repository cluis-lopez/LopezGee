package com.lopezgee.auth;

import java.util.HashMap;

public interface DataBaseIF {
	public User getUser(String id);
	public void createUser(User u);
	public void updateToken(User u, String newToken);
	public void close();
	public HashMap<String, String> getInfo();
}