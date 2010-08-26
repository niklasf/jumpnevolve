package com.googlecode.jumpnevolve.game.objects;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Der jumpingSoldier ist ein Sodier, der springt, wenn er wieder den Boden
 * berührt.
 * 
 * @author Erik Wagner
 * 
 */
public class JumpingSoldier extends Soldier {

	public JumpingSoldier(World world, Shape shape, float mass) {
		super(world, shape, mass);
		// TODO shape durch center ersetzen und super ein spezifisches Shape
		// übergeben
		// TODO Masse festlegen
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		super.specialSettingsPerRound(input);
		if (this.isWayBlocked(Shape.UNTEN)) {
			this.setVelocity(Vector.UP.mul(39.24f)); // Sprung nach oben
		}
	}
}
