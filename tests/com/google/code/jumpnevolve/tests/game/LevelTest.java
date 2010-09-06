package com.google.code.jumpnevolve.tests.game;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.Engine;

public class LevelTest {

	public static void main(String[] args) {
		
		Engine engine = Engine.getInstance();
		
		Level level = Levelloader.asyncLoadLevel("resources/demo-levels/test.txt");
		engine.switchState(level);
		
		engine.start();
	}
}
