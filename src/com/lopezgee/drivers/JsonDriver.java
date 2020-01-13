package com.lopezgee.drivers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lopezgee.auth.DataBaseIF;
import com.lopezgee.auth.User;

public class JsonDriver implements DataBaseIF {
	JsonDriverVars jvars;
	File datafile;
	List<User> users;

	private Logger log = Logger.getLogger("JsonDriver");
	private FileHandler fh;

	public JsonDriver(String props) {

		try {
			fh = new FileHandler("JsonDriver.log", true);
		} catch (SecurityException | IOException e1) {
			System.err.println("No se puede abrir el fichero de log");
			e1.printStackTrace();
		}

		log.setUseParentHandlers(false); // To avoid console logging
		log.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);

		Gson json = new Gson();
		jvars = null;
		FileReader fr;

		// Loading props file
		try {
			fr = new FileReader(props);
			jvars = json.fromJson(fr, JsonDriverVars.class);
			fr.close();
		} catch (IOException e) {
			log.log(Level.SEVERE, "Cannot open properties file. Exiting");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			System.err.println("Json Driver: No se puede abrir el fichero de propiedades");
		}

		try {
			datafile = new File(jvars.DataPath + "/" + jvars.DataFile);
			fr = new FileReader(datafile);
			users = json.fromJson(fr, new TypeToken<ArrayList<User>>() {
			}.getType());
			fr.close();
		} catch (IOException e) {
			log.log(Level.INFO, "Cannot open or initialize Database from Json file");
			log.log(Level.INFO, "Assuming there's no Database file yet");
			log.log(Level.INFO, "Using an empty Database instead. Close properly this Database to store it");
			users = new ArrayList<>();
		}
	}

	public String close() {
		String temp = update();
		users = null;
		return temp;
	}
	
	public String update() {
		Gson json = new GsonBuilder().setPrettyPrinting().create();
		String ret = "";
		try {
			FileWriter fw = new FileWriter(datafile);
			fw.write(json.toJson(users));
			fw.close();
			ret = "OK";
		} catch (IOException e) {
			log.log(Level.SEVERE, "Cannot update close and store Database into Json file");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			ret = "Cannot update & close DataBase";
		}
		return ret;
	}
	
	public String deleteDatabase() {
		String ret = "";
		if (datafile.delete()) {
			ret = "OK";
			log.log(Level.WARNING, "Deleted the database file "+datafile.toPath().toString());
		} else {
			ret = "Failed to delete database file";
			log.log(Level.WARNING, "Cannot delete the database file "+datafile.toPath().toString());
		}
		
		return ret;
		
	}

	public User getUser(String id) {
		User user = null;
		for (User u : users) {
			if (u.Id.equals(id)) {
				user = u;
				break;
			}
		}
		return user;
	}

	public String createUser(User user) {
		String ret = "";
		if (!userExists(user.Id)) {
			users.add(user);
			log.log(Level.INFO, "Added new user with id: " + user.Id);
			ret = "OK";
		} else {
			log.log(Level.INFO, "The user with id " + user.Id + " is already in the database");
			ret = "User already exists";
		}
		return ret;
	}

	public String deleteUser(User u) {
		String ret = "";
		if (u != null && userExists(u.Id)) {
			String temp = u.Id;
			users.remove(u);
			log.log(Level.INFO, " The user with id " + temp + " was removed from the database");
			ret = "OK";
		} else {
			log.log(Level.INFO, " The user does not exist");
			ret = "User does not exist";
		}
		return ret;
	}

	public HashMap<String, String> getInfo() {
		HashMap<String, String> info = new HashMap<>();
		info.put("DatabaseFile", jvars.DataPath + "/" + jvars.DataFile);
		info.put("UsersInDatabase", Integer.toString(users.size()));
		info.put("DatabaseSizeOnDisk", Long.toString(datafile.length()));
		return info;
	}

	public String updateToken(User u, String newToken) {
		String ret = "";
		if (userExists(u.Id)) { //Warning check for token already valid
			u.Token = newToken;
			long t = (new Date()).getTime();
			t += jvars.TokenValidDays + 7 * 24 * 60 * 60 * 1000; // Tiempo de validez del token en milisegundos
			u.TokenValidUpTo = new Date(t);
			ret = "OK";
		} else {
			ret = "User does not exist";
		}
		return ret;
	}
	
	public String updateUser(User u, User newUserData) {
		String ret = "";
		if (userExists(u.Id)) { //We'll replace all user data except id and "User Since". Leave Token invalid
			u.Name = newUserData.Name;
			u.Mail = newUserData.Mail;
			u.Password = newUserData.Password; //Warning ... should be encrypted before
			u.Salt = newUserData.Salt;
			u.Token = "";
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 1900);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			u.TokenValidUpTo = cal.getTime();
			ret = "OK";
		} else {
			ret = "User does not exist";
		}
		return ret;
	}

	private boolean userExists(String id) {
		boolean ret = false;
		for (User u : users) {
			if (u.Id.equals(id)) {
				ret = true;
				break;
			}
		}
		return ret;

	}
}
