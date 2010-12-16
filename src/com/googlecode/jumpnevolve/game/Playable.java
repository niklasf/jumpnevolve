/**
 * 
 */
package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;

/**
 * @author Erik Wagner
 * 
 */
public interface Playable extends Drawable, Pollable {

	final int DIRECTION_RIGHT = 0;
	final int DIRECTION_LEFT = 1;

	/**
	 * Lässt das Objekt springen, soll nur möglich sein, wenn sich das Objekt am
	 * Boden befindet
	 */
	public void jump();

	/**
	 * Lässt das Objekt in eine Richtung laufen
	 * 
	 * @param direction
	 *            Die Richtung (eine der DIRECTION-Konstanten aus Playable)
	 */
	public void run(int direction);

	/**
	 * Springt in der Luft zum zweiten Mal
	 */
	public void doubleJump();
}
