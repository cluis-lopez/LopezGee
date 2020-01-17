package com.lopezgee.restserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.lopezgee.auth.AuthServer;

public class Server {

	private static Map<String, Servlet> mountPoints;
	private static Logger log = Logger.getLogger("LopezGee");
	private static FileHandler fd;

	public static void main(String[] args) {

		// Inicializando Logger
		System.setProperty("java.util.logging.SimpleFormatter.format", 
				"%1$tF %1$tT %4$s %5$s%6$s%n");
		
		try {
			fd = new FileHandler("logs/LopezGeeServer.log", true);
		} catch (SecurityException | IOException e1) {
			System.err.println("No se puede abrir el fichero de log");
			e1.printStackTrace();
		}

		log.setUseParentHandlers(false); // To avoid console logging
		log.addHandler(fd);
		SimpleFormatter formatter = new SimpleFormatter();
		fd.setFormatter(formatter);

		// Inicializando fichero de propiedades
		Gson json = new Gson();
		FileReader reader = null;
		
		try {
			reader = new FileReader("etc/Properties.json");
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "Cannot open properties file. Exiting");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			System.err.println("No se puede abrir e ficherp de propiedades");
		}
		
		ExtVars props = json.fromJson(reader, ExtVars.class);
		
		// Mapeando puntos de montaje //
		mountPoints = new HashMap<>();
		if (props.Servlets.isEmpty()) {
			log.log(Level.INFO, "No mounted servlets");
		} else {
			for (Servlet s: props.Servlets) {
				mount(s.MountPoint, props.AppPath + "." + s.ClassName, s);
			}
		}
			
		//Arrancando el sistema de autentificación
		
		AuthServer authserver = new AuthServer(props.AuthDriver, props.DataBasePropsFile, log);

		// Inicializacion del subsistemna de accouting
		
		FileWriter accnt = null;
		try {
			accnt = new FileWriter (props.AccountingFile, true);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Cannot open the accounting file " + props.AccountingFile);
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}
		
		ServerSocket server = null;
		
		try {
			server = new ServerSocket(props.Port);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Cannot start Server Socket at port: " + props.Port);
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			System.err.println("No se puede arrancar el server en el puerto " + props.Port);
			e.printStackTrace();
		}
		
		System.out.println("Arrancando el servidor");
		log.log(Level.INFO, "Server started");
		log.log(Level.INFO, "Listening at port: " + props.Port);

		Socket client = null;
		while (true) { // NOSONAR
			try {
				client = server.accept();
			} catch (IOException e) {
				log.log(Level.WARNING, "Cannot launch thread to accept client: " + client.toString());
				log.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
			}
			final APIServer request = new APIServer(client, mountPoints, log, authserver, accnt);
			Thread thread = new Thread(request);
			thread.start();
		}
	}

	private static void mount(String URI, String m, Servlet s) {
		try {
			s.cl = Class.forName(m);
			mountPoints.put(URI, s);
			log.log(Level.INFO, "Mounting URI " + URI + " into " + m);
			System.out.println("La URI " + URI + " apunta al servlet " + s.cl.getCanonicalName());
		} catch (ClassNotFoundException e) {
			System.err.println("No se puede mapear el servlet: " + m);
			log.log(Level.WARNING, "Cannot map servlet: " + m);
			log.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
		}
	}

}
