package com.school.proto.analyser;

import java.io.PrintWriter;

public interface AbstractAnalyser {
	public boolean analyseLevel1(String fileUrl,String baseUrl,PrintWriter writer);
	public boolean analyseLevel2(String fileUrl,String baseUrl,PrintWriter writer);
}
