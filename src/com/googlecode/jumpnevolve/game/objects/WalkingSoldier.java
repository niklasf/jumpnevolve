package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.world.Moving;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 *
 * Beschreibung: Ein Soldat der nach rechts und links läuft
 *
 * Spezifikationen: siehe Soldier
 *
 * Bewegungen: siehe Soldier; bewegt sich nach rechts und links und prallt von
 * Wänden ab; Geschwindigkeit: 10 Pixel pro Sekunde
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
public class WalkingSoldier extends Soldier implements Moving {

	private static final long serialVersionUID = 3329079316071279296L;
	private Vector curDirection = Vector.ZERO;

	public WalkingSoldier(World world, Vector position) {
		super(world, position);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		super.specialSettingsPerRound(input);
		if (this.isWayBlocked(Shape.RIGHT)) {
			this.curDirection = Vector.LEFT;
		}
		if (this.isWayBlocked(Shape.LEFT)) {
			this.curDirection = Vector.RIGHT;
		}
		if (this.getVelocity().x == 0.0f && this.isWayBlocked(Shape.DOWN)) {
			this.curDirection = Vector.RIGHT;
		}
	}

	@Override
	public Vector getMovingDirection() {
		return this.curDirection;
	}

	@Override
	public float getMovingSpeed() {
		return 10.0f;
	}
}
