package com.clopez.restserver;

import java.util.Map;

public interface MiniServlet {
	public String[] doGet(Map<String, String> map);
}
