package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Collision;
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
 * Beschreibung: Ein lebender Ball (Radius: 65), der rollt
 * 
 * Spezifikationen: blockbar, nicht schiebbar
 * 
 * Bewegungen: Masse = 5, Laufen nach rechts und links, Springen (0.5 sec)
 * 
 * Aggressivitäten: gegen alle Gegner (EnemyTemplate) beim Draufspringen
 * 
 * Immunitäten: keine1
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
		super(world, new Circle(position, 6.5f), 5.0f, true, true, true);
		// TODO: Masse in Ordunng?
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// Tod überprüfen
		if (this.isAlive() == false) {
			this.replace(this.getLastSave());
			// Zurücksetzen zum letzten Speicherort

			this.setAlive(true); // Wiederbeleben

		}
		// Bewegungen
		this.setVelocity(new Vector(0, this.getVelocity().y));
		if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
			this.setVelocity(new Vector(100, this.getVelocity().y));
			// Nach rechts laufen
		} else if (input.isKeyDown(Input.KEY_LEFT)
				|| input.isKeyDown(Input.KEY_S)) {
			this.setVelocity(new Vector(-100, this.getVelocity().y));
			// Nach links laufen
		}

		if ((input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W))
				&& this.isWayBlocked(Shape.UNTEN)) {
			this.setVelocity(new Vector(this.getVelocity().x, -0.5f * 98.1f
					* this.getMass()));
			// Springen für 0.5 Sekunden bis Stillstand
		}
		// Schwerkraft
		this.applyForce(Vector.DOWN.mul(98.1f * this.getMass()));
	}

	@Override
	public void kill(AbstractObject killer) {
		// Sterben
		this.setAlive(false);
	}

	@Override
	public void onLivingCrash(AbstractObject other) {
		Collision col = this.getShape().getCollision(other.getShape());
		if (other instanceof EnemyTemplate && col.isBlocked(Shape.UNTEN)
				&& col.isBlocked(Shape.RECHTS) == false
				&& col.isBlocked(Shape.LINKS) == false) {
			other.kill(this);
		}
	}

	// TODO: draw-Methode einfügen

	@Override
	public String toDataLine() {
		// FIXME: Bitte überprüfee, geht das so mit den Vektoren
		return new String("RollingBall_" + this.getPosition() + "_"
				+ this.getName() + "_none_none\n");
	}
}
