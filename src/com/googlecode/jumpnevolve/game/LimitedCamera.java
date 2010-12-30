/**
 * 
 */
package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine Kamera, die über die Möglichkeit verfügt, die Position so zu begrenzen,
 * dass man nichts "außerhalb" des Levels sieht
 * 
 * @author Erik Wagner
 * 
 */
public interface LimitedCamera extends Camera {

	/**
	 * Limitiert die Position der Kamera
	 * 
	 * @param position
	 *            Die ursprüngliche Position der Kamera bzw. die zu limitierende
	 *            Position
	 * @param world
	 *            Die Welt, nach der sich die Limitierung richten soll
	 * @return Die limitierte Position
	 */
	public Vector limitPosition(Vector position, World world);
}
