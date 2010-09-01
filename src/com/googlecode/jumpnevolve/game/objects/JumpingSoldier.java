package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

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

	private static final long serialVersionUID = -753627347562694811L;

	public JumpingSoldier(World world, Vector position) {
		super(world, shape, mass);
		// TODO shape durch position erzeugen und super übergeben
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
