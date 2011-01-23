/**
 * 
 */
package com.googlecode.jumpnevolve.game.player;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.math.NextShape;

/**
 * @author Erik Wagner
 * 
 */
public interface Playable extends Drawable {

	final int STAY = 0;
	final int DIRECTION_RIGHT = 1;
	final int DIRECTION_LEFT = 2;

	/**
	 * @return Die Geschwindigkeit mit der sich das Objekt bewegt
	 */
	public float getWalkingSpeed();

	/**
	 * @return Die Höhe in Pixeln (bei Zoom = 1), die das Objekt hochspringt
	 */
	public float getJumpingHeight();

	public boolean isBlockable();

	public boolean isLiving();

	public boolean isDamageable();

	public NextShape getShape();
}
