package com.googlecode.jumpnevolve.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Zum Laden von Leveln.
 * 
 * @author Erik Wagner
 * 
 */
public class Levelloader {

	private String source;

	private Level level;

	public Levelloader(String source) {
		this.source = source;
	}

	public void run() {
		try {
			BufferedReader file = new BufferedReader(new FileReader(source));
			// Level laden und in this.level speichern
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Level getLevel() {
		return this.level;
	}

	public static Level asyncLoadLevel(String source) {
		Levelloader loader = new Levelloader(source);
		loader.run();
		return loader.getLevel();
	}
}

class Attribute {

	final String motherString;
	private String[] partStrings;
	private int currentAttribute;

	/**
	 * Erzeugt ein neues Attribut für ein Objekt, das Attribut kann aus mehreren
	 * Teilen bestehen.
	 * 
	 * @param attributeString
	 *            Der String, aus dem die Attribute entnommen werden
	 */
	public Attribute(String attributeString) {
		motherString = attributeString;
		int count = 1;
		for (int i = 0; i < motherString.length(); i++) {
			if (motherString.charAt(i) == ',') {
				count++;
			}
		}
		partStrings = new String[count];
		for (int i = 0; i < count; i++) {
			for (int j = 0; this.motherString.charAt(i) != ','; j++) {
				this.partStrings[i] += this.motherString.charAt(i);
			}
		}
	}

	public String[] getAllAttributes() {
		return this.partStrings;
	}

	public String getNextAttribut() {
		String s = this.partStrings[currentAttribute];
		if (this.currentAttribute < this.getAttributeCount() - 1) {
			this.currentAttribute++;
			return s;
		} else {
			// FIXME: Fehlermeldung ausgeben, da keine weiteren Attribute
			// vorhanden
			return new String();
		}
	}

	public void switchToAttribute(int attribteIndex) {
		if (attribteIndex >= 0 && attribteIndex <= this.getAttributeCount()) {
			this.currentAttribute = attribteIndex;
		} else {
			// FIXME: Fehlermeldung ausgeben, da falscher Index
		}
	}

	public int getAttributeCount() {
		return this.partStrings.length;
	}
}
