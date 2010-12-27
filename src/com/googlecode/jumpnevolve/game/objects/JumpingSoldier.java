package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * 
 * Beschreibung: Ein Soldat der springt
 * 
 * Spezifikationen: siehe Soldier
 * 
 * Bewegungen: siehe Soldier; springt, wenn er den Boden berührt
 * 
 * Aggressivitäten: siehe Soldier
 * 
 * Immunitäten: siehe Soldier
 * 
 * Aktivierung: keine
 * 
 * Deaktivierung: keine
 * 
 * Besonderheiten: keine
 * 
 * @author Erik Wagner
 * 
 */
public class JumpingSoldier extends Soldier {

	private static final long serialVersionUID = -753627347562694811L;

	public JumpingSoldier(World world, Vector position) {
		super(world, position);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		super.specialSettingsPerRound(input);
		if (this.isWayBlocked(Shape.DOWN)) {
			this.setVelocity(Vector.UP.mul(this.getMass() * 0.1f * 98.1f)); // Sprung
			// nach
			// oben
		}
	}
}
