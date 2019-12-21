package com.clopez.restserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class doFile {

	public doFile() {
	}
	
	public String[] doGet(String resource) {
		String[] ret = new String[2];
		ret[0] = "";
		try {
			ret[0] = Files.probeContentType(Paths.get(resource));
			ret[1] = new String (Files.readAllBytes(Paths.get(resource)));
		} catch (FileNotFoundException e) {
			ret[0]="";
			ret[1] = "File not found";
		} catch (IOException e) {
			ret[0]="";
			ret[1] = "Cannot read file";
		}
		
		return ret;
	}

}
