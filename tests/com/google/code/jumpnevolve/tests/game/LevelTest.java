package com.google.code.jumpnevolve.tests.game;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.SlickEngine;

public class LevelTest {

	public static void main(String[] args) {

		SlickEngine engine = SlickEngine.getInstance();
		engine.setTargetFrameRate(100);

		Level level = Levelloader
				.asyncLoadLevel("resources/demo-levels/demo.txt");
		engine.switchState(level);

		engine.start();
	}
}
