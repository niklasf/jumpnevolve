package com.googlecode.jumpnevolve.graphics.world;

import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public interface Moving {

	/**
	 * Gibt die Geschwindigkeit zurück, mit der sich das Objekt in x-Richtung
	 * bewegen möchte
	 * 
	 * @return >0.0f
	 */
	public float getMovingSpeed();

	/**
	 * Gibt die Richtung zurück, in die sich das Objekt bewegt
	 * 
	 * @return Eine der Konstanten aus {@link Shape} ({@link Shape#NULL}, wenn
	 *         zur Zeit keine Bewegung stattfindet)
	 */
	public Vector getMovingDirection();
}