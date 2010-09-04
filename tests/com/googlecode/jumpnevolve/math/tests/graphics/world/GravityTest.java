package com.googlecode.jumpnevolve.math.tests.graphics.world;

import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.RollingBall;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * <p>
 * Einfacher Testfall für die Physikengine.
 * <p>
 * 
 * <p>
 * Erwartetes Verhalten: Die Spielfigur fällt nach unten auf den Boden und
 * bleibt dort liegen.
 * </p>
 * 
 * @author Niklas Fiekas
 */
public class GravityTest {

	/**
	 * @param args
	 *            Kommandozeilenargumente werden ignoriert.
	 */
	public static void main(String[] args) {
		Engine engine = Engine.getInstance();

		World world = new World(10, 10, 1);

		world.add(new Ground(world, new Vector(1.5f, 2.0f), new Vector(1.0f,
				0.5f)));
		world.add(new RollingBall(world, new Vector(1.5f, 1.0f)));

		engine.switchState(world);
		engine.start();
	}
}
