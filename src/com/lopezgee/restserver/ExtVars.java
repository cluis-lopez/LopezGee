package com.lopezgee.restserver;

import java.util.ArrayList;
import java.util.List;


public class ExtVars {
	public int Port;
	public String Root;
	public String AppPath;
	public String AuthDriver;
	public String DataBasePropsFile;
	public String AccountingFile;
	public List<Servlet> Servlets;
	
	public ExtVars() {
		Servlets = new ArrayList<>();
		Port = 0;
		Root = "";
		AppPath = "";
		AuthDriver = "";
		DataBasePropsFile = "";
		AccountingFile = "";
	}
	
	@Override
	public String toString() {
		String ret = "Port: " + Port + "\n"
		+ "Root Web Server: " + Root + "\n"
		+ "Auth Driver: " + AuthDriver + "\n"
		+ "Servlets Java Path: " + AppPath + "\n"
		+ "Mount Points" +"(" + Servlets.size() + ")\n";
		for (Servlet s : Servlets)
			ret = ret + s.MountPoint + " : " + s.ClassName + "Auth: " + s.Auth + "\n";
		return ret;
	}
}
