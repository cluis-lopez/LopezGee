package com.lopezgee.restserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lopezgee.auth.AuthServer;

public class APIServer implements Runnable {

	private static final String newLine = "\r\n";
	private Logger log;
	private Map<String, Servlet> servlets;
	private Map<String, String> headerFields;
	private String body;
	private Socket socket;
	private AuthServer auth;
	private FileWriter accntfile;
	private Map<String, Long> timers;

	public APIServer(Socket s, Map<String, Servlet> map, Logger log, AuthServer auth, FileWriter accntfile) {
		socket = s;
		servlets = map;
		headerFields = new HashMap<>();
		body = null;
		this.log = log;
		this.auth = auth;
		this.accntfile = accntfile;
		timers = new HashMap<>();
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

		if (!servlets.containsKey(req.resource)) { // No declared miniservlet, expect a file
			doFile df = new doFile(log);
			ret = df.doGet(req.resource.substring(1));
			if (ret[0].equals("")) { // No file found
				resp = "HTTP/1.0 404 " + ret[1] + newLine + newLine;
			} else {
				resp = "HTTP/1.0 200 OK" + newLine + "Content-Type: " + ret[0] + newLine + "Date: " + new Date()
						+ newLine + "Content-length: " + ret[1].length() + newLine + newLine + ret[1];
			}

		} else {
			Object ob = null;

			// Chequeamos si se requiere autentificacion y en su caso, si es válida

			boolean validToken = servlets.get(req.resource).Auth && req.params.get("User") != null
					&& req.params.get("Token") != null
					&& auth.isValidToken(req.params.get("User"), req.params.get("Token"));

			if (!servlets.get(req.resource).Auth || validToken) {
				try {
					Constructor<?> cons = servlets.get(req.resource).cl.getConstructor();
					ob = cons.newInstance(null);
					ob.getClass().getMethod("initialize", Logger.class, AuthServer.class).invoke(ob, log, auth);
					ret = (String[]) ob.getClass().getMethod("doGet", Map.class).invoke(ob, req.params);
					timers = (Map<String, Long>) ob.getClass().getMethod("destroy").invoke(ob, null);
					if (servlets.get(req.resource).Account)
						writeAccntLine(servlets.get(req.resource).MountPoint, req.params.get("User"));
					
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					log.log(Level.SEVERE, "Cannot instantiate servlet");
					log.log(Level.SEVERE, e.toString());
					log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
				}
				resp = "HTTP/1.0 200 OK" + newLine + "Content-Type: " + ret[0] + newLine + "Date: " + new Date()
						+ newLine + "Content-length: " + ret[1].length() + newLine + newLine + ret[1];
			} else {
				resp = "HTTP/1.0 401 Unauthorized" + newLine + newLine;
			}
		}
		return resp;
	}

	private String processPost(HeaderDecoder req) {
		String[] ret = new String[2];
		String resp = "";
		BodyDecoder bd = null;
		Object ob = null;

		if (!servlets.containsKey(req.resource)) { // No declared miniservlet, invalid request
			resp = "HTTP/1.0 404 Servlet Not Declared or Found" + newLine + newLine;
		}

		try {
			Constructor<?> cons = servlets.get(req.resource).cl.getConstructor();
			ob = cons.newInstance(null);
			if (headerFields.get("Content-Type") != null
					&& headerFields.get("Content-Type").equals("application/x-www-form-urlencoded")) {
				bd = new BodyDecoder(body);
				ob.getClass().getMethod("initialize", Logger.class, AuthServer.class).invoke(ob, log, auth);
				ret = (String[]) ob.getClass().getMethod("doPost", Map.class).invoke(ob, bd.params);
				timers = (Map<String, Long>) ob.getClass().getMethod("destroy").invoke(ob, null);
				if (servlets.get(req.resource).Account)
					writeAccntLine(servlets.get(req.resource).MountPoint, req.params.get("User"));
				
				resp = "HTTP/1.0 200 OK" + newLine + "Content-Type: " + ret[0] + newLine + "Date: " + new Date()
						+ newLine + "Content-length: " + ret[1].length() + newLine + newLine + ret[1];
			} else {
				resp = "HTTP/1.0 404 Improper post request" + newLine + newLine;
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | UnsupportedEncodingException e) {
			log.log(Level.SEVERE, "Cannot instantiate servlet");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}

		return resp;
	}
	
	private void writeAccntLine(String servletName, String userName) {
		if (userName == null || userName.equals(""))
			userName = "Anonymous";
		SimpleDateFormat dtformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		String line = MessageFormat.format("{0} {1} {2}", dtformat.format(new Date()), servletName, userName);
		for (String s : timers.keySet())
			line += " " + s + " " + timers.get(s);
		line += "\n";
		try {
			accntfile.write(line);
			accntfile.flush();
		} catch (IOException e) {
			log.log(Level.SEVERE, "Cannot write account entry");
			log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
		}
	}
}
