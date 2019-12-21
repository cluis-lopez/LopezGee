package com.clopez.restserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

	private static Map<String, Class> mountPoints;
	
	public Server() {
	}

	public static void main(String[] args) throws Exception {
		// Mapeando puntos de montaje //
		mountPoints = new HashMap<>();
		mount ("/Tester", "com.clopez.restserver.Tester");
		
		final ServerSocket server = new ServerSocket(8080);
		System.err.println("Arrancando el servidor");
		Socket client = null;
        while (true) { //NOSONAR
            client = server.accept();
            final APIServer request = new APIServer(client, mountPoints);
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
