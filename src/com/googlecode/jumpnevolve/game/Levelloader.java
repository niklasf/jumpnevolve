package com.googlecode.jumpnevolve.game;

/**
 * Zum Laden von Leveln.
 * 
 * @author Erik Wagner
 * 
 */
public class Levelloader {

	/**
	 * Lädt ein Level und gibt dieses zurück.
	 * 
	 * @param source
	 *            Die Datei, aus dem das Level geladen wird
	 * @return Das geladene Level
	 */
	public static Level loadLevel(String source) {
		return new Level(0, 0, 0);
	}
}
