package com.google.code.jumpnevolve.tests.game;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.SlickEngine;

public class LevelTest {

	public static void main(String[] args) {

		SlickEngine engine = SlickEngine.getInstance();
		engine.setTargetFrameRate(100);

		Level level = Levelloader
				.asyncLoadLevel("editor/levels/Level-1.txt");
		engine.switchState(level);

		engine.start();
	}
}
