package com.googlecode.jumpnevolve.tests.graphics;

import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.world.World;

/**
 * <p>
 * Testfall für die Grafikengine.
 * <p>
 *
 * <p>
 * Erwartetes Verhalten ist das Anzeigen eines leeren Fensters.
 * </p>
 *
 * @author Niklas Fiekas
 */
public class EmptyWorldTest {

	/**
	 * @param args
	 *            Kommandozeilenargumente werden ignoriert.
	 */
	public static void main(String[] args) {
		// Neue, leere Welt erzeugen
		Engine.getInstance().addState(new World(0, 0, 0));

		// Engine starten
		Engine.getInstance().start();
	}

}
