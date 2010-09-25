package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.objects.Soldier;
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
public class WalkingSoldier extends Soldier {

	private static final long serialVersionUID = 3329079316071279296L;

	public WalkingSoldier(World world, Vector position) {
		super(world, position);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		super.specialSettingsPerRound(input);
		// TODO: Geschwindigkeiten anpassen
		if (this.isWayBlocked(Shape.RECHTS)) {
			this.setVelocity(Vector.LEFT.mul(10.0f));
			// Richtung von Rechts nach Links ändern
		}
		if (this.isWayBlocked(Shape.LINKS)) {
			this.setVelocity(Vector.RIGHT.mul(10.0f));
			// Richtung von Links nach Rechts ändern
		}
		if (this.getVelocity().x == 0.0f && this.isWayBlocked(Shape.UNTEN)) {
			this.setVelocity(Vector.RIGHT.mul(10.0f));
		}
	}

	@Override
	public String toDataLine() {
		// FIXME: Bitte überprüfee, geht das so mit den Vektoren
		return new String("WalkingSoldier_" + this.getPosition() + "_"
				+ this.getName() + "_none_none\n");
	}
}
