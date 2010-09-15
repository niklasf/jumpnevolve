package com.googlecode.jumpnevolve.util;

public class LineAdapter implements LineListener {

	@Override
	public boolean lineEmpty() {
		return true;
	}

	@Override
	public boolean lineFailed(String line) {
		throw new LineFailedException(line);
	}

	@Override
	public boolean lineParsed(String line, String[] parts) {
		return true;
	}

}
