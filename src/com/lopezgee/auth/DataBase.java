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
		} catch (IllegalArgumentException  e) {
			log.log(Level.SEVERE, "Illegal arguments to instantiate");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		} catch (InvocationTargetException e) {
			log.log(Level.SEVERE, "Invocation Target Exception (problema al instanciar el driver) ");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getCause().getStackTrace()));
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}
	}
	
	public String deleteDatabase() {
		String ret = "";
		try {
			Method me = ob.getClass().getMethod("deleteDatabase");
			ret = (String) me.invoke(ob, null);
		} catch (InvocationTargetException e) {
			log.log(Level.SEVERE, "getInfo: Exception in invoked method");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret ="Something went wrong";
		} catch (NoSuchMethodException |SecurityException | IllegalAccessException | IllegalArgumentException e) {
			log.log(Level.SEVERE, "getInfo: Invocation exception");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret ="Something went wrong";
		}
		return ret;
	}
	public User getUser(String id) {
		User user = null;
		try {
			Method me = ob.getClass().getMethod("getUser", String.class);
			user = (User) me.invoke(ob, id);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.log(Level.SEVERE, "Cannot read user from Database");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}
		
		return user;
		
	};
	
	public String createUser(User u) {
		String ret = "";
		try {
			Method me = ob.getClass().getMethod("createUser", User.class);
			ret = (String) me.invoke(ob, u);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.log(Level.SEVERE, "Cannot create user in Database");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret ="Something went wrong";
		}
		return ret;
	}
	
	public String updateToken(User u, String newToken) {
		String ret = "";
		try {
			Method me = ob.getClass().getMethod("updateToken", User.class, String.class);
			ret = (String) me.invoke(ob, u, newToken);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.log(Level.SEVERE, "Cannot update token in Database");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret ="Something went wrong";
		}
		return ret;
	}
	
	public String update() {
		String ret = "";
		try {
			Method me = ob.getClass().getMethod("update");
			ret = (String) me.invoke(ob, null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.log(Level.SEVERE, "Cannot update Database");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret ="Something went wrong";
		}
		return ret;
	}
	
	public String close() {
		String ret = "";
		try {
			Method me = ob.getClass().getMethod("close");
			ret = (String) me.invoke(ob, null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.log(Level.SEVERE, "Cannot update token in Database");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret ="Something went wrong";
		}
		return ret;
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
	
	public String deleteUser(User u) {
		String ret = "";
		try {
			Method me = ob.getClass().getMethod("deleteUser", User.class);
			ret = (String) me.invoke(ob, u);
		} catch (InvocationTargetException e) {
			log.log(Level.SEVERE, "deleteUser: Exception in invoked method");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret ="Something went wrong";
		} catch (NoSuchMethodException |SecurityException | IllegalAccessException | IllegalArgumentException e) {
			log.log(Level.SEVERE, "deleteUser: Invocation exception");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret ="Something went wrong";
		}
		return ret;
	}
	
	public String updateUser(User u, User newUserData) {
		String ret = "";
		try {
			Method me = ob.getClass().getMethod("updateUser", User.class, User.class);
			ret = (String) me.invoke(ob, u, newUserData);
		} catch (InvocationTargetException e) {
			log.log(Level.SEVERE, "deleteUser: Exception in invoked method");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret ="Something went wrong";
		} catch (NoSuchMethodException |SecurityException | IllegalAccessException | IllegalArgumentException e) {
			log.log(Level.SEVERE, "deleteUser: Invocation exception");
			log.log(Level.SEVERE, e.getMessage());
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret ="Something went wrong";
		}
		return ret;
	}
}
