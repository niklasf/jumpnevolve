package com.googlecode.jumpnevolve.graphics;

public abstract class Engine {
	private static AbstractEngine instance;
	
	public static AbstractEngine getInstance() {
		if(instance == null) {
			instance = SlickEngine.getInstance();
		}
		return instance;
	}
	
	public static void makeCurrent(AbstractEngine current) {
		instance = current;
	}
}
