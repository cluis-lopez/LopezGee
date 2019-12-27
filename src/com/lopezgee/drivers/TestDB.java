package com.lopezgee.drivers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.lopezgee.auth.User;

public class TestDB {

	public static void main(String[] args) {
		
		System.setProperty("java.util.logging.SimpleFormatter.format", 
				"%1$tF %1$tT %4$s %5$s%6$s%n");
		
		FileHandler fd = null;;
		Logger log = Logger.getLogger("TestDB");
		
		try {
			fd = new FileHandler("TestDB.log");
		} catch (SecurityException | IOException e1) {
			System.err.println("No se puede abrir el fichero de log");
			e1.printStackTrace();
		}

		log.setUseParentHandlers(true); // To keep console logging
		log.addHandler(fd);
		SimpleFormatter formatter = new SimpleFormatter();
		fd.setFormatter(formatter);
		
		DataBase db = new DataBase("com.lopezgee.drivers.JsonDriver", "JsonDriver.json", log);
		User user = new User("cluis.lopez@gmail.com", "Carlos", "1234");
		db.createUser(user);
		db.close();
		
		// Add a second user
		
		db = new DataBase("com.lopezgee.drivers.JsonDriver", "JsonDriver.json", log);
		user = new User("pepito@gmail.com", "Pepito", "4231");
		db.createUser(user);
		db.close();
		
	}

}
