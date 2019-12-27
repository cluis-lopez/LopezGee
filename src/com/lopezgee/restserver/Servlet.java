package com.lopezgee.restserver;

public class Servlet {
	String MountPoint;
	String ClassName;
	Class cl;
	boolean Auth;
	boolean Account;

	@Override
	public String toString() {
		String ret = "MountPoint: " + MountPoint + "\n";
		ret = ret + "ClassName: " + ClassName + "\n";
		ret = ret + "Class: " + cl.getCanonicalName();
		ret = ret + "Auth: " + Auth + "\n";
		ret = ret + "Accounting: " + Account + "\n";
		return ret;
	}
}
