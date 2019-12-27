package com.lopezgee.drivers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lopezgee.auth.User;

public class JsonDriver {
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
			System.err.println("Json Driver: No se puede abrir e fichero de propiedades");
		}

		try {
			fr = new FileReader(jvars.DataFile);
			Users = json.fromJson(fr, new TypeToken<ArrayList<User>>() {
			}.getType());
		} catch (FileNotFoundException e) {
			log.log(Level.INFO, "Cannot open or initialize Database from Json file");
			log.log(Level.INFO, "Assuming there's no Database file yet");
			log.log(Level.INFO, "Using an empty Database instead. Close properly this Database to store it");
			Users = new ArrayList<>();
		}
	}

	public void close() {
		Gson json = new GsonBuilder().setPrettyPrinting().create();

		try {
			FileWriter fw = new FileWriter(jvars.DataFile);
			fw.write(json.toJson(Users));
			fw.close();
		} catch (IOException e) {
			log.log(Level.SEVERE, "Cannot close and store Database into Json file");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}
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

	public void createUser(User user) {
		if (!userExists(user.Id)) {
			Users.add(user);
			log.log(Level.INFO, "Added new user with id: " + user.Id);
		} else {
			log.log(Level.INFO, " The user with id " + user.Id + "is already in the database");
		}
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
