package com.clopez.restserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {

	private static Map<String, Class> mountPoints;
	private static Logger log = Logger.getLogger("LopezGee");
	private static FileHandler fd;
	private static Properties props = new Properties();

	public static void main(String[] args) throws Exception {

		// Inicializando Logger
		System.setProperty("java.util.logging.SimpleFormatter.format", 
				"%1$tF %1$tT %4$s %5$s%6$s%n");
		
		try {
			fd = new FileHandler("LopezGeeServer.log");
		} catch (SecurityException | IOException e1) {
			System.err.println("No se puede abrir el fichero de log");
			e1.printStackTrace();
		}

		log.setUseParentHandlers(false); // To avoid console logging
		log.addHandler(fd);
		SimpleFormatter formatter = new SimpleFormatter();
		fd.setFormatter(formatter);

		// Inicializando fichero de propiedades
		InputStream in = null;
		try {
			in = new FileInputStream("Properties");
			BufferedInputStream fd = new BufferedInputStream(in);
			props.load(fd);
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "Error de entrada/salida al abrir el fichero de Propiedades");
			log.log(Level.SEVERE, e.toString(), e);
		}

		// Mapeando puntos de montaje //
		mountPoints = new HashMap<>();
		mount("/Tester", "com.clopez.restserver.Tester");

		final ServerSocket server = new ServerSocket(Integer.parseInt(props.getProperty("Port")));
		System.out.println("Arrancando el servidor");
		log.log(Level.INFO, "Server started");

		Socket client = null;
		while (true) { // NOSONAR
			client = server.accept();
			final APIServer request = new APIServer(client, mountPoints, log);
			Thread thread = new Thread(request);
			thread.start();
		}
	}

	private static void mount(String URI, String className) {
		Class cl = null;
		try {
			cl = Class.forName(className);
			mountPoints.put(URI, cl);
			System.out.println("La URI " + URI + " apunta al servlet " + className);
		} catch (ClassNotFoundException e) {
			System.err.println("No se puede mapear el servlet");
			e.printStackTrace();
		}
	}

}
