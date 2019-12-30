package com.lopezgee.auth;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase implements DataBaseIF {
	private Class<?> cl;
	private Object ob;
	private Logger log;
	
	public DataBase(String driver, String props, Logger log) {
		try {
			this.log = log;
			cl = Class.forName(driver);
			Constructor<?> cons = cl.getConstructor(String.class);
			ob = cons.newInstance(props);
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE, "Cannot load or instantiate database Class");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		} catch (NoSuchMethodException  e) {
			log.log(Level.SEVERE, "Cannot invoke database Method");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		} catch (InstantiationException | IllegalAccessException e) {
			log.log(Level.SEVERE, "Instantiation Execption or Illegal Access");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		} catch (IllegalArgumentException | InvocationTargetException e) {
			log.log(Level.SEVERE, "Illegal arguments to instantiate");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}
	}
	public User getUser(String id) {
		User user = null;
		try {
			Method me = cl.getMethod("getUser", String.class);
			user = (User) me.invoke(id);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.log(Level.SEVERE, "Cannot read user from Database");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}
		
		return user;
		
	};
	
	public void createUser(User u) {
		try {
			Method me = ob.getClass().getMethod("createUser", User.class);
			me.invoke(ob, u);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.log(Level.SEVERE, "Cannot create user in Database");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}
	}
	
	public void updateToken(User u, String newToken) {
		try {
			Method me = cl.getMethod("updateToken", User.class, String.class);
			me.invoke(u, newToken);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.log(Level.SEVERE, "Cannot update token in Database");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}	
	}
	
	public void close() {
		try {
			Method me = ob.getClass().getMethod("close");
			me.invoke(ob, null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.log(Level.SEVERE, "Cannot update token in Database");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}	
	}
	
	public HashMap<String, String> getInfo() {
		HashMap<String, String> m = null;
		try {
			Method me = ob.getClass().getMethod("getInfo");
			m = (HashMap<String, String>) me.invoke(ob, null);
		} catch (InvocationTargetException e) {
			log.log(Level.SEVERE, "getInfo: Exception in invoked method");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		} catch (NoSuchMethodException |SecurityException | IllegalAccessException | IllegalArgumentException e) {
			log.log(Level.SEVERE, "getInfo: Invocation exception");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		} 
		return m;
	}
	
	public void deleteUser(User u) {
		try {
			Method me = ob.getClass().getMethod("deleteUser");
			me.invoke(ob, null);
		} catch (InvocationTargetException e) {
			log.log(Level.SEVERE, "getInfo: Exception in invoked method");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		} catch (NoSuchMethodException |SecurityException | IllegalAccessException | IllegalArgumentException e) {
			log.log(Level.SEVERE, "getInfo: Invocation exception");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		} 
	}
}
