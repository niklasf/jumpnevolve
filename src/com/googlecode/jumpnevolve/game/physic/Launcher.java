package com.googlecode.jumpnevolve.game.physic;

import com.googlecode.jumpnevolve.game.ObjectFocusingCamera;
import com.googlecode.jumpnevolve.graphics.AbstractEngine;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
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

		World world = new PhysicsWorld(1000, 1000, 10, 0.01f);

		// Electron e = new Electron(world, new Vector(0, 100),
		// new Vector(0.1f, 0)), e2 = new Electron(world, new Vector(0,
		// 100), new Vector(0.4f, 0)), e3 = new Electron(world,
		// new Vector(0, 100), new Vector(0.2f, 0));
		// HomogenEField eField = new HomogenEField(world,
		// ShapeFactory.createCircle(Vector.ZERO, 1), Vector.UP.mul(0.2f),
		// true);
		// HomogenBField bField = new HomogenBField(world,
		// ShapeFactory.createCircle(Vector.ZERO, 1), 1, true);
		// world.add(e);
		// world.add(e2);
		// world.add(e3);
		// world.add(eField);
		// world.add(bField);
		// world.setCamera(new ObjectFocusingCamera(e3));
		// world.add(new Follower(world, e, 1000));
		// world.add(new Follower(world, e2, 1000));
		// world.add(new Follower(world, e3, 1000));
		//
		// world.setZoom(200, 200);

		ElectronEmitter emit = new ElectronEmitter(world, new Vector(0, 100),
				0.1f);
		HomogenEField eF1 = new HomogenEField(world,
				ShapeFactory.createRectangle(new Vector(100, 100), 200, 200),
				Vector.RIGHT.mul(15.0f), false), eF2 = new HomogenEField(
				world,
				ShapeFactory.createRectangle(new Vector(4200, 100), 8000, 1000),
				Vector.UP.mul(5.0f), false);
		HomogenBField bF1 = new HomogenBField(
				world,
				ShapeFactory.createRectangle(new Vector(4200, 100), 8000, 1000),
				0.363f, false);
		world.add(emit);
		world.add(eF1);
		world.add(eF2);
		world.add(bF1);
		world.setCamera(new Camera() {

			@Override
			public Vector getPosition() {
				return new Vector(400, 100);
			}
		});

		// engine.setTargetFrameRate(Parameter.GAME_FPS_TARGET);
		engine.switchState(world);
		engine.start();
	}
}
