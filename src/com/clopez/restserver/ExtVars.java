package com.clopez.restserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtVars {
	public int Port;
	public String Root;
	public String AppPath;
	public List<Servlet> Servlets;
	
	public ExtVars() {
		Servlets = new ArrayList<>();
		Port = 0;
		Root="";
		AppPath="";
	}
	
	@Override
	public String toString() {
		String ret = "Port: " + Port +"\n"
		+ "Root Web Server: " + Root +"\n"
		+ "Servlets Java Path: " + AppPath +"\n"
		+ "Mount Points" +"(" + Servlets.size() +")\n";
		for (Servlet s : Servlets)
			ret = ret + s.MountPoint + " : " + s.ClassName + "Auth: " + s.Auth + "\n";
		return ret;
	}
}
