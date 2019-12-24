package com.clopez.restserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtVars {
	public int Port;
	public String Root;
	public String AppPath;
	public List<Map<String, String>> Servlets;
	
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
		for (Map<String, String> m: Servlets)
			ret = ret + m.get("MountPoint") + " : " + m.get("ClassName") + "\n";
		
		return ret;
	}
}
