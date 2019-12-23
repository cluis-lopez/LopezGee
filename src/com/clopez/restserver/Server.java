package com.clopez.restserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
		Gson json = new Gson();
		FileReader reader = new FileReader("Properties.json");
		Type listType = new TypeToken<ExtVars>(){}.getType();
		ExtVars props = json.fromJson(reader, listType);
		
		// Mapeando puntos de montaje //
		mountPoints = new HashMap<>();
		if (props.Servlets.size() ==0) {
			log.log(Level.INFO, "No mounted servlets");
		} else {
			for (Map<String, String> m: props.Servlets) {
				mount(m.get("MountPoint"), props.AppPath + "." + m.get("ClassName"));
				log.log(Level.INFO, "Mounting URI " + m.get("MountPoint") + " into " + props.AppPath + "." + m.get("ClassName"));
			}
		}
			
		//mount("/Tester", "com.clopez.restserver.Tester");

		final ServerSocket server = new ServerSocket(props.Port);
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
