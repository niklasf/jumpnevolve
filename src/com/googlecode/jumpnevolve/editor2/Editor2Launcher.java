package com.googlecode.jumpnevolve.editor2;

import java.io.IOException;

import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.AbstractEngine;
import com.googlecode.jumpnevolve.graphics.Engine;

public class Editor2Launcher {

	/**
	 * Startet den neuen Editor.
	 * 
	 * @param args
	 *            Kommandozeilenargumente
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		AbstractEngine engine = Engine.getInstance();

		engine.setTargetFrameRate(100);
		engine.switchState(new Editor2(new Levelloader("resources/levels/default.txt"), 100, 100, 1));
		engine.start();
	}
}
