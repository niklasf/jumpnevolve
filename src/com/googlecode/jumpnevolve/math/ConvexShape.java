package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

/**
 * Neue allgemeine Klasse für geometrische Figuren
 * 
 * @author Erik Wagner
 * 
 */
interface ConvexShape extends NextShape {

	public AxisProjection projectOnAxis(Vector axis);

	/**
	 * Ein Array mit den Achsen, die bei der Kollision überprüft werden müssen
	 * 
	 * WICHTIG: Die Achsen müssen normiert sein
	 * 
	 * @param other
	 * @return
	 */
	public ArrayList<Vector> getAxises(ConvexShape other);
}
