package com.googlecode.jumpnevolve.game;

/**
 * Zum Laden von Leveln.
 * 
 * @author Erik Wagner
 * 
 */
public class Levelloader implements Runnable {
	
	private String source;
	
	private Level level;
	
	public Levelloader(String source) {
		this.source = source;
	}

	public void run() {
		// Level laden und in this.level speichern
	}
	
	public Level getLevel() {
		return this.level;
	}
	
	public static Level asyncloadLevel(String source) {
		Levelloader loader = new Levelloader(source);
		loader.run();
		return loader.getLevel();
	}
}
