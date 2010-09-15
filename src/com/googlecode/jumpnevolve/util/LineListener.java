package com.googlecode.jumpnevolve.util;

public interface LineListener {
	
	public boolean lineParsed(String line, String[] parts);
	
	public boolean lineEmpty();
	
	public boolean lineFailed(String line);
}
