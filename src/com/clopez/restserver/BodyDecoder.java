package com.clopez.restserver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class BodyDecoder {
	public Map<String, String> params;
	
	public BodyDecoder(String body) throws UnsupportedEncodingException{
		params = new HashMap<>();
		for (String s : body.split("&")) {
			int idx = s.indexOf("=");
			params.put(URLDecoder.decode(s.substring(0, idx), "UTF-8"),
					URLDecoder.decode(s.substring(idx + 1), "UTF-8"));
		}
	}
}
