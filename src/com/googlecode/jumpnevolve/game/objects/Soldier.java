package com.googlecode.jumpnevolve.game.objects;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Soldat der auf der Stelle steht. Dient hauptsächlich als Super-Klasse,
 * kann aber auch als stehender Soldat benutzt werden.
 * 
 * Soldaten können nur den Spieler töten und nich durch andere Soldaten getötet
 * werden.
 * 
 * Ein Soldat ist blockbar und spürt immer die Schwerkraft.
 * 
 * @author Erik Wagner
 * 
 */
public class Soldier extends EnemyTemplate {

	public Soldier(World world, Shape shape, float mass) {
		super(world, shape, mass, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void specialSettingsPerRound() {
		if (this.isAlive() == false) {
			this.getWorld().removeFromAllLists(this);
			// Aus allen Listen der Welt löschen, da dieses Objekt besiegt wurde
		}
		this.applyForce(Vector.DOWN.mul(this.getMass() * 9.81f)); // Schwerkarft
	}

	public void onLivingCrash(AbstractObject other) {
		// Soldier kann nur den Spieler töten, wenn dieser nicht oberhalb von
		// ihm ist
		if (other instanceof FigureTemplate) {
			if (this.getShape().getTouchedSideOfThis(other.getShape()) != Shape.OBEN) {
				other.kill(this);
			}
		}
	}

	public void kill(AbstractObject killer) {
		// Soldier kann nicht durch andere Soldaten getötet werden
		if (killer instanceof Soldier == false) {
			if (killer instanceof FigureTemplate) {
				// TODO: Punkte oder ähnliches für den Spieler zählen
			}
			this.setAlive(false);
			// Lebensstatus auf tot setzen, da der Gegner getötet wurde
		}
	}

	// TODO: draw-Methode einfügen
}
