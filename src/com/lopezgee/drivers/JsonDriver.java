package com.lopezgee.drivers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	List<User> Users;

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
			Users = json.fromJson(fr, new TypeToken<ArrayList<User>>() {
			}.getType());
			fr.close();
		} catch (IOException e) {
			log.log(Level.INFO, "Cannot open or initialize Database from Json file");
			log.log(Level.INFO, "Assuming there's no Database file yet");
			log.log(Level.INFO, "Using an empty Database instead. Close properly this Database to store it");
			Users = new ArrayList<>();
		}
	}

	public String update() {
		return close();
	}
	
	public String close() {
		Gson json = new GsonBuilder().setPrettyPrinting().create();
		String ret = "";
		try {
			FileWriter fw = new FileWriter(datafile);
			fw.write(json.toJson(Users));
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
		for (User u : Users) {
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
			Users.add(user);
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
			Users.remove(u);
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
		info.put("UsersInDatabase", Integer.toString(Users.size()));
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

	private boolean userExists(String id) {
		boolean ret = false;
		for (User u : Users) {
			if (u.Id.equals(id)) {
				ret = true;
				break;
			}
		}
		return ret;

	}
}
