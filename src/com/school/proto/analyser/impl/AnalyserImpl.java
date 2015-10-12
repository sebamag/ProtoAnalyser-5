package com.school.proto.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import com.school.proto.analyser.AbstractAnalyser;

public class AnalyserImpl implements AbstractAnalyser{

	private static final String VERSION_TARGETDURATION = "#EXT-X-TARGETDURATION"; //Changed this EXT-X-TARGETDURATION
	private static final String EXT_X_VERSION = "#EXT-X-VERSION";
	private static final String EXTM3U2 = "#EXTM3U";
	private static final String EXT_X_STREAM_INF = "#EXT-X-STREAM-INF:";
	private static final String EXTM3U = EXTM3U2;

	@Override
	public boolean analyseLevel1(String fileName,String baseUrl,PrintWriter writer) {
		URL fileUrl;
		try {
			fileUrl = new URL(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(fileUrl.openStream()));
			int i = 0;
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
			if(i == 0) {
				if(!inputLine.equalsIgnoreCase(EXTM3U)){
					writer.println("Master Playlist: ERR01    ||    VALIDATION ISSUE                 ||    "    +fileName+    "    ||    Tag Validation Error");
				} 
			}
			if(i == 1 || i==3) {
				if(!inputLine.startsWith(EXT_X_STREAM_INF)){
					writer.println("Master Playlist: ERR02    ||    VALIDATION ISSUE                 ||    "    +fileName+    "    ||    Tag Validation Error");
				}
			}
						
			if(i == 2 || i==4 || i==6 ||i==8) {
				String fileContents = getFileContents(baseUrl+inputLine);
				if(fileContents != null){
					//writer.println("Level1: Contentns of the file  "+fileName);
					//writer.println(fileContents);
				}
				else {
					writer.println("Master Playlist:  ERR03    ||    URL NOT AVAILABLE                   ||    "    +(baseUrl+inputLine) +    "    ||    URL Error In Master Playlist");
				}
			}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean analyseLevel2(String fileName,String baseUrl,PrintWriter writer) {
		URL fileUrl;
		int targetDuration = 0;
		String strTargetDuration = "";;
		String strTduration ="";
		String lastLine = "";
		try {
			fileUrl = new URL(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(fileUrl.openStream()));
			int i = 0;
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				lastLine = inputLine;
			if(i == 0) {
				if(!inputLine.equalsIgnoreCase(EXTM3U2)){
					writer.println("Level2 Playlist:  ERR21    ||    VALIDATION ISSUE                    ||    "    +fileName+    "               ||    Tag Validation Error");
				}
			}
			if(i == 1) {
			if(!inputLine.startsWith(EXT_X_VERSION)){
					
				    writer.println("Level2 Playlist:  ERR22    ||    VALIDATION ISSUE                       ||    "    +fileName+    "              ||    Tag Validation Error");
				}else { 
				try {
						int versionNumber = Integer.parseInt(inputLine.substring(inputLine.indexOf(":")+1));
					}
					catch(Exception e) {
			        writer.println("Level2 Playlist:  ERR23    ||    VERSION ISSUE                       ||    "    +fileName+    "              ||    Version Tag Error");
					}
			}
			}
			
			if(i == 2) {
				if(!inputLine.startsWith(VERSION_TARGETDURATION)){ //Changed this EXT-X-TARGETDURATION
					
					writer.println("Level2 Playlist:  ERR24    ||    VERSION ISSUE                            ||    "    +fileName+    "          ||    Version Tag Error");
				}else {
					try {
						strTargetDuration = inputLine.substring(inputLine.indexOf(":")+1);
						targetDuration = Integer.parseInt(strTargetDuration);
					}
					catch(Exception e) {
				    writer.println("Level2 Playlist:  ERR25    ||    TARGET DURATION ISSUE        ||    "    +fileName+    "            ||    Target Duration Number Error");
					}
				}
			}
		
			if(i==4) {
				if(!inputLine.startsWith("#EXTINF:")){
					
					writer.println("Level2 Playlist:  ERR26    ||    EXTINF ISSUE                                ||    "    +fileName+    "          ||    EXTINF Tag Not Found");
				}
				else {
					try {
						strTduration  = inputLine.substring(inputLine.indexOf(":")+1,inputLine.indexOf(",")-1);
						int tDuration = Integer.parseInt(strTduration);
						if(!strTargetDuration.equals(strTduration)) {
					writer.println("Level2 Playlist:  ERR27    ||    EXTINF ISSUE                                ||    "    +fileName+    "     ||    EXTINF Not Match Target Duration");
						}
					}
					catch(Exception e) {
					writer.println("Level2 Playlist:  ERR28    ||    EXTINF ISSUE                                ||    "    +fileName+    "         ||    EXTINF Duration Error");
					}
				}
			}
				i++;
			}
			if(lastLine.startsWith("#EXT-X-ENDLISTX")) {
				   writer.println("Level2 Playlist:  ERR29    ||    #EXT-X-ENDLIST ISSUE              ||    "    +fileName+    "         ||    #EXT-X-ENDLIST Tag Error");
				   writer.println("Level2 Playlist:  ERR30    ||    #EXT-X-ENDLIST ISSUE              ||    "    +fileName+    "         ||    #EXT-X-ENDLIST Tag not found in this Level" );
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public String getFileContents(String fileName) {
		StringBuffer fileContents = new StringBuffer("");
		try {
			URL fileUrl = new URL(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(fileUrl.openStream()));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				fileContents.append(inputLine).append("\n");
			}
		}
		catch(Exception e) {
			return null;	
		}
		return fileContents.toString();
	}

}
