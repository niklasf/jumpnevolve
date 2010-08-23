package com.googlecode.jumpnevolve.game.objects;

import com.googlecode.jumpnevolve.game.objects.Soldier;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Soldier der sich horizontal bewegt und von blockbaren Gegenständen
 * abprallt.
 * 
 * @author Erik Wagner
 * 
 */
public class WalkingSoldier extends Soldier {

	public WalkingSoldier(World world, Shape shape, float mass) {
		super(world, shape, mass);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void specialSettingsPerRound() {
		super.specialSettingsPerRound();
		// TODO: Geschwindigkeiten anpassen
		if (this.isWayBlocked(Shape.RECHTS)) {
			this.setVelocity(Vector.LEFT.mul(10.0f));
			// Richtung von Rechts nach Links ändern
		}
		if (this.isWayBlocked(Shape.LINKS)) {
			this.setVelocity(Vector.RIGHT.mul(10.0f));
			// Richtung von Links nach Rechts ändern
		}
	}
}
