package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

//Weitere Möglichkeit zur Spezifizierung der Objekte
//Beschreibung --> Beschreibung des Objekts in groben Zügen, Erscheinungsform
//Spezifikationen --> blockbar, schiebbar
//Bewegungen --> nur bei sich bewegenden Objekten, Masse angeben
//Aggressivitäten --> nur bei tötenden Objekten, --> daraus folgt, dass killable = true
//Immunitäten --> nur bei lebenden Objekten --> daraus folgt, dass living = true
//Aktivierung --> was passiert, wenn das Objekt aktiviert wird und durch wen kann das Objekt aktiviert werden --> daraus folgt, dass activable = true
//Deaktiierung --> was passiert, wenn das Objekt deaktiviert wird
//Besonderheiten --> alle sonstigen Abweichungen (z.B. in Bezug auf Schiebe- und Blockeigenschaften)

/**
 * 
 * Beschreibung: Ein lebender Ball, der rollt
 * 
 * Spezifikationen: blockbar, nicht schiebbar
 * 
 * Bewegungen: Masse = 5, Laufen nach rechts und links, Springen (0.5 sec)
 * 
 * Aggressivitäten: gegen alle Gegner (EnemyTemplate) beim Draufspringen
 * 
 * Immunitäten: keine
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
public class RollingBall extends FigureTemplate {

	private static final long serialVersionUID = -6066789739733255353L;

	public RollingBall(World world, Vector position) {
		super(world, shape, mass, true, true, true);
		// FIXME: Shape erstellen
		// TODO: Masse festlegen
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// Tod überprüfen
		if (this.isAlive() == false) {
			this.replace(this.getLastSave()); // Zurücksetzen zum letzten
			// Speicherort

			// Bei PlayerTemplate replace() und getLastSave() sowie
			// newSavePlace() erstellen

			this.setAlive(true); // Wiederbeleben

		}
		// Bewegungen
		if (input.RIGHT) {
			this.setVelocity(new Vector(3, this.getVelocity().y)); // Nach
			// rechts
			// laufen
		} else if (input.RIGHT) {
			this.setVelocity(new Vector(-3, this.getVelocity().y)); // Nach
			// links
			// laufen
		}

		if (input.UP && this.isWayBlocked(Shape.UNTEN)) {
			this.setVelocity(new Vector(this.getVelocity().x, 0.5f * 9.81f));
			// Springen für 0.5 Sekunden bis Stillstand
		}
		// Schwerkraft
		this.applyForce(Vector.DOWN.mul(9.81f));
	}

	@Override
	public void kill(AbstractObject killer) {
		// Sterben
		this.setAlive(false);
	}

	@Override
	public void onLivingCrash(AbstractObject other) {
		if (other instanceof EnemyTemplate
				&& this.getShape().getTouchedSideOfThis(other.getShape()) == Shape.UNTEN) {
			other.kill(this);
		}
	}

}
