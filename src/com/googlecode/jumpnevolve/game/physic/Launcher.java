package com.googlecode.jumpnevolve.game.physic;

import com.googlecode.jumpnevolve.game.ObjectFocusingCamera;
import com.googlecode.jumpnevolve.graphics.AbstractEngine;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

public class Launcher {

	/**
	 * Startet das Spiel.
	 * 
	 * @param args
	 *            Kommandozeilenargumente
	 */
	public static void main(String[] args) {
		AbstractEngine engine = Engine.getInstance();

		World world = new World(1000, 1000, 10);
		Electron e = new Electron(world, new Vector(0, 100), new Vector(0.025f,
				0)), e2 = new Electron(world, new Vector(0, 100), new Vector(
				0.8f, 0)), e3 = new Electron(world, new Vector(0, 100),
				new Vector(0.2f, 0));
		world.add(e);
		world.add(e2);
		world.add(e3);
		world.setCamera(new ObjectFocusingCamera(e));
		world.add(new Follower(world, e));
		world.add(new Follower(world, e2));
		world.add(new Follower(world, e3));

		// engine.setTargetFrameRate(Parameter.GAME_FPS_TARGET);
		engine.switchState(world);
		engine.start();
	}

}
