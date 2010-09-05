package com.google.code.jumpnevolve.tests.game;

import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.WalkingSoldier;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * <p>
 * Einfacher Testfall für die WalkingSoldier Klasse.
 * <p>
 * 
 * <p>
 * Erwartetes Verhalten: Die Soldaten laufen und drehen um, wenn er Sie auf ein
 * anderes Objekt treffen.
 * </p>
 * 
 * FIXME: Von 100 Zusammenstößen zwischen den Soldaten wird mindestens einer
 * so gelöst, dass sie aneinander vorbeilaufen anstatt abzuprallen.
 * Möglichen Fehler in getTouchedSideOfThis korrigieren.s
 * 
 * @author Niklas Fiekas
 */
public class WalkingSoldierTest {

	/**
	 * @param args
	 *            Kommandozeilenargumente werden ignoriert.
	 */
	public static void main(String[] args) {
		Engine engine = Engine.getInstance();

		World world = new World(10, 10, 1);

		// Topf
		world.add(new Ground(world, new Vector(1.5f, 2.5f), new Vector(3.0f,
				0.5f)));
		world.add(new Ground(world, new Vector(0.0f, 2.5f), new Vector(0.4f, 1.0f)));
		world.add(new Ground(world, new Vector(3.0f, 2.5f), new Vector(0.4f, 1.0f)));
		
		// Soldaten
		world.add(new WalkingSoldier(world, new Vector(1.5f, -10.0f)));
		world.add(new WalkingSoldier(world, new Vector(2.0f, -100.0f)));
		
		engine.switchState(world);
		engine.start();
	}
}