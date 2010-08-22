package com.googlecode.jumpnevolve.game.objects;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Der jumpingSoldier ist ein Gegner, der springt, wenn er wieder den Boden
 * berührt.
 * 
 * Er kann nur von einem Spieler getötet werden und kann auch nur diesen töten
 * 
 * @author Erik Wagner
 * 
 */
public class JumpingSoldier extends EnemyTemplate {

	public JumpingSoldier(World world, Shape shape, float mass) {
		super(world, shape, mass, true);
		// TODO shape durch center ersetzen und super ein spezifisches Shape
		// übergeben
		// TODO Masse festlegen
	}

	@Override
	protected void specialSettingsPerRound() {
		if (this.isAlive() == false) {
			this.getWorld().removeFromAllLists(this);
			// Aus allen Listen der Welt löschen, da dieses Objekt besiegt wurde
		}
		if (this.isWayBlocked(Shape.UNTEN)) {
			this.setVelocity(Vector.UP.mul(39.24f)); // Sprung nach oben
		}
		this.applyForce(Vector.DOWN.mul(this.getMass() * 9.81f)); // Schwerkarft
	}

	public void onLivingCrash(AbstractObject other) {
		// JunpingSoldier kann nur den Spieler töten, wenn dieser nicht oberhalb
		// von ihm ist
		if (other instanceof FigureTemplate) {
			if (this.getShape().getTouchedSideOfThis(other.getShape()) != Shape.OBEN) {
				other.kill(this);
			}
		}
	}

	public void kill(AbstractObject killer) {
		// JumpingSoldier kann nur durch den Spieler getötet werden
		if (killer instanceof FigureTemplate) {
			// TODO: Punkte oder ähnliches für den Spieler zählen
			this.setAlive(false);
			// Lebensstatus auf tot setzen, da der Gegner durch den Spieler
			// besiegt wurde
		}
	}
}
