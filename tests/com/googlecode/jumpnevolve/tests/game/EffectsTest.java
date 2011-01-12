package com.googlecode.jumpnevolve.tests.game;

import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.WalkingSoldier;
import com.googlecode.jumpnevolve.graphics.SlickEngine;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

public class EffectsTest {
	public static void main(String[] args) {
		
		SlickEngine engine = SlickEngine.getInstance();
		engine.setTargetFrameRate(100);
		
		World world = new World(1000, 300, 1);
		world.setZoom(3000);
		
		world.add(new Ground(world, new Vector(300, 300), new Vector(800, 20)));
		
		engine.switchState(world);
		
		engine.start();
	}
}
