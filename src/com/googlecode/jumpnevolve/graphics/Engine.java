package com.googlecode.jumpnevolve.graphics;

public abstract class Engine {
	private static SlickEngine instance;

	public static SlickEngine getInstance() {
		if (instance == null) {
			instance = SlickEngine.getInstance();
		}
		return instance;
	}

	public static void makeCurrent(SlickEngine current) {
		instance = current;
	}
}
