package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * 
 * Beschreibung: Eine Killermaschine, die alles tötet, was sie berührt
 * 
 * Spezifikationen: blockbar, nicht schiebbar
 * 
 * Bewegungen: Masse = 20; grundsätzlich keine Bewegung; Schwerkraft wirkt
 * 
 * Aggressivitäten: nur gegen alles, wenn das nicht von oben kommt
 * 
 * Immunitäten: keine
 * 
 * Aktivierung: keine
 * 
 * Deaktivierung: keine
 * 
 * Besonderheiten: kann als Vorlage für andere Killermaschinen benutzt werden
 * 
 * @author Erik Wagner
 * 
 */
public class KillingMachine extends EnemyTemplate {

	private static final long serialVersionUID = -5724600752326575341L;

	public KillingMachine(World world, Vector position) {
		super(world, new Rectangle(position, new Vector(28, 28)), 20.0f, true);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.isAlive() == false) {
			this.getWorld().removeFromAllLists(this);
			// Aus allen Listen der Welt löschen, da dieses Objekt besiegt wurde
		}
		this.applyForce(Vector.DOWN.mul(this.getMass() * 9.81f)); // Schwerkarft
	}

	@Override
	public void onLivingCrash(AbstractObject other) {
		// KillingMachine versucht alle lebenden Objekte zu töten, wenn diese
		// nicht oberhalb von ihr sind
		if (this.getShape().getCollision(other.getShape(), other.isMoveable(),
				this.isMoveable()).isBlocked(Shape.UP) == false) {
			other.kill(this);
		}
	}

	@Override
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
