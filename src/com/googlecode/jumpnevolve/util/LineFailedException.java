package com.googlecode.jumpnevolve.util;

public class LineFailedException extends RuntimeException {

	private static final long serialVersionUID = 8945755199789337059L;
	
	public LineFailedException(String line) {
		super(String.format("Line failed: %s", line));
	}

}
