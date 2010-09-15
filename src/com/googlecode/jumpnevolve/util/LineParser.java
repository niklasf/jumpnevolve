package com.googlecode.jumpnevolve.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineParser {
	
	private final Pattern pattern;
	
	private LineListener lineListener;
	
	public LineParser(String regex) {
		this(regex, null);
	}
	
	public LineParser(String regex, LineListener lineListener) {
		this.pattern = Pattern.compile(regex);
		this.lineListener = lineListener;
	}
	
	public void parse(Reader reader) throws IOException {
		parse(reader, lineListener);
	}
	
	public void parse(Reader reader, LineListener listener) throws IOException {
		// BufferedReader erzeugen
		BufferedReader buffer;
		if(reader instanceof BufferedReader) {
			buffer = (BufferedReader) reader;
		} else {
			buffer = new BufferedReader(reader);
		}
		
		// Jede Zeile parsen
		String line;
		while((line = buffer.readLine()) != null) {
			if(!line.matches("\\s*")) {
				Matcher matcher = pattern.matcher(line);
				if(matcher.matches()) {
					String[] matches = new String[matcher.groupCount()];
					for(int i = 0; i < matches.length; i++) {
						matches[i] = matcher.group(i);
					}
					if(!listener.lineParsed(line, matches)) {
						return;
					}
				} else {
					if(!listener.lineFailed(line)) {
						return;
					}
				}
			} else {
				if(!listener.lineEmpty()) {
					return;
				}
			}
		}
	}
}
