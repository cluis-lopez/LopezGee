package com.clopez.restserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class APIServer implements Runnable {

	private static final String newLine = "\r\n";
	private Logger log;
	private Map<String, Class> mountPoints;
	private Map<String, String> headerFields;
	private String body;
	private Socket socket;

	public APIServer(Socket s, Map<String, Class> m, Logger log) {
		socket = s;
		mountPoints = m;
		headerFields = new HashMap<>();
		body = null;
		this.log = log;
	}

	@Override
	public void run() {
		try {
			processRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processRequest() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		OutputStream out = new BufferedOutputStream(socket.getOutputStream());
		PrintStream pout = new PrintStream(out);

		// read first line of request
		String request = in.readLine();
		if (request == null || request.length() == 0)
			return;

		// Rest of Header
		while (true) {
			String header = in.readLine();
			if (header == null || header.length() == 0)
				break;
			headerFields.put(header.split(":", 2)[0].trim(), header.split(":", 2)[1].trim());
		}

		// Body, if any
		if (headerFields.get("Content-Length") != null && Integer.parseInt(headerFields.get("Content-Length")) > 0) {
			StringBuilder sb = new StringBuilder();
			char[] cb = new char[Integer.parseInt(headerFields.get("Content-Length"))];
			int bytesread = in.read(cb, 0, Integer.parseInt(headerFields.get("Content-Length")));
			sb.append(cb, 0, bytesread);
			body = sb.toString();
		}

		HeaderDecoder reqLine = new HeaderDecoder(request);
		// Logging

		log.log(Level.INFO, "Serving {0}",
				reqLine.command + " " + reqLine.resource + " from " + headerFields.get("Origin"));

		String response = "";

		boolean reqValid = (reqLine.command.equals("GET") || reqLine.command.equals("POST"))
				&& (reqLine.protocol.equals("HTTP/1.0") || reqLine.protocol.equals("HTTP/1.1"));
		if (reqValid && reqLine.command.equals("GET"))
			response = processGet(reqLine);
		if (reqValid && reqLine.command.equals("POST"))
			response = processPost(reqLine);
		if (!reqValid) // Bad Request
			response = "HTTP/1.0 400 Bad Request" + newLine + newLine;

		pout.print(response);
		pout.close();
		out.close();
		in.close();

	}

	private String processGet(HeaderDecoder req) {
		String[] ret = new String[2];
		String resp;

		if (!mountPoints.containsKey(req.resource)) { // No declared miniservlet, expect a file
			doFile df = new doFile();
			ret = df.doGet(req.resource.substring(1));
			if (ret[0].equals("")) { // No file found
				resp = "HTTP/1.0 404 " + ret[1] + newLine + newLine;
			} else {
				resp = "HTTP/1.0 200 OK" + newLine + "Content-Type: " + ret[0] + newLine + "Date: " + new Date()
						+ newLine + "Content-length: " + ret[1].length() + newLine + newLine + ret[1];
			}

		} else {
			Object ob = null;
			try {
				ob = mountPoints.get(req.resource).newInstance();
				ret = (String[]) ob.getClass().getMethod("doGet", Map.class).invoke(ob, req.params);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				log.log(Level.SEVERE, "Cannot instantiate servlet");
				log.log(Level.SEVERE, e.toString());
			}
			resp = "HTTP/1.0 200 OK" + newLine + "Content-Type: " + ret[0] + newLine + "Date: " + new Date() + newLine
					+ "Content-length: " + ret[1].length() + newLine + newLine + ret[1];
		}

		return resp;

	}

	private String processPost(HeaderDecoder req) {
		String[] ret = new String[2];
		String resp = "";
		BodyDecoder bd = null;
		Object ob = null;

		if (!mountPoints.containsKey(req.resource)) { // No declared miniservlet, invalid request
			resp = "HTTP/1.0 404 Servlet Not Declared or Found" + newLine + newLine;
		}

		try {
			ob = mountPoints.get(req.resource).newInstance();
			if (headerFields.get("Content-Type") != null
					&& headerFields.get("Content-Type").equals("application/x-www-form-urlencoded")) {
				bd = new BodyDecoder(body);
				ret = (String[]) ob.getClass().getMethod("doPost", Map.class).invoke(ob, bd.params);
				resp = "HTTP/1.0 200 OK" + newLine + "Content-Type: " + ret[0] + newLine + "Date: " + new Date()
						+ newLine + "Content-length: " + ret[1].length() + newLine + newLine + ret[1];
			} else {
				resp = "HTTP/1.0 404 Improper post request" + newLine + newLine;
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | UnsupportedEncodingException e) {
			log.log(Level.SEVERE, "Cannot instantiate servlet");
			log.log(Level.SEVERE, e.toString());
		}

		return resp;
	}
}
