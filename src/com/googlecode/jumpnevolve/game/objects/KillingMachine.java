package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine Tötungsmaschine, die versucht alle zu töten, was ihr begegnet. Sie ist
 * blockbar und töten von allen Seiten außer von unten (anderes Objekt
 * oberhalb).
 * 
 * Dient hauptsächlich als Super-Klasse, kann aber auch als stehende Gefahr
 * benutzt werden.
 * 
 * Sie verspürt immer die Schwerkraft.
 * 
 * @author Erik Wagner
 * 
 */
public class KillingMachine extends EnemyTemplate {

	public KillingMachine(World world, Shape shape, float mass,
			boolean blockable) {
		super(world, shape, mass, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.isAlive() == false) {
			this.getWorld().removeFromAllLists(this);
			// Aus allen Listen der Welt löschen, da dieses Objekt besiegt wurde
		}
		this.applyForce(Vector.DOWN.mul(this.getMass() * 9.81f)); // Schwerkarft
	}

	public void onLivingCrash(AbstractObject other) {
		// KillingMachine versucht alle lebenden Objekte zu töten, wenn diese
		// nicht oberhalb von ihr sind
		if (this.getShape().getTouchedSideOfThis(other.getShape()) != Shape.OBEN) {
			other.kill(this);
		}
	}

	public void kill(AbstractObject killer) {
		// KillingMachine kann von allen Seiten und allen Objekten getötet
		// werden
		if (killer instanceof FigureTemplate) {
			// TODO: Punkte oder ähnliches für den Spieler zählen
		}
		this.setAlive(false);
		// Lebensstatus auf tot setzen, da der Gegner getötet wurde
	}

	// TODO: draw-Methode einfügen

}
