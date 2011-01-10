/**
 * 
 */
package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Soldat, der immer wieder so hoch springt, wie er vor dem Fallen war
 * 
 * @author Erik Wagner
 * 
 */
public class SpringingSoldier extends Soldier {

	/**
	 * @param world
	 * @param position
	 */
	public SpringingSoldier(World world, Vector position) {
		super(world, position);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		super.specialSettingsPerRound(input);
		if (this.isWayBlocked(Shape.DOWN)) {
			this.setVelocity(Vector.UP.mul(this.getOldVelocity().abs()));
			// Sprung nach oben
		}
	}

}
