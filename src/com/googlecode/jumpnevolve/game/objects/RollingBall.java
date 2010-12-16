package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.game.Playable;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Collision;
import com.googlecode.jumpnevolve.math.Rectangle;
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
		super(world, new Circle(position, 30.0f), 5.0f, true, true, true);
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
		// Bewegung in x-Richtung auf 0 setzen
		this.setVelocity(new Vector(0, this.getVelocity().y));
		// Schwerkraft
		this.applyForce(Vector.DOWN.mul(98.1f * this.getMass()));

		// Bewegungen TODO: Entfernen
		if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
			this.setVelocity(new Vector(100, this.getVelocity().y));
			// Nach rechts laufen
		} else if (input.isKeyDown(Input.KEY_LEFT)
				|| input.isKeyDown(Input.KEY_S)) {
			this.setVelocity(new Vector(-100, this.getVelocity().y));
			// Nach links laufen
		}
		// Springen TODO: Entfernen
		if ((input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W))
				&& this.isWayBlocked(Shape.DOWN)) {
			this.setVelocity(new Vector(this.getVelocity().x, -0.25f * 98.1f
					* this.getMass()));
			// Springen für 0.5 Sekunden bis Stillstand
		}
	}

	@Override
	public void kill(AbstractObject killer) {
		// Sterben
		this.setAlive(false);
	}

	@Override
	public void onLivingCrash(AbstractObject other) {
		Collision col = this.getShape().getCollision(other.getShape(),
				other.isMoveable(), true);
		if (other instanceof EnemyTemplate && col.isBlocked(Shape.DOWN)
				&& col.isBlocked(Shape.RIGHT) == false
				&& col.isBlocked(Shape.LEFT) == false) {
			other.kill(this);
		}
	}

	@Override
	public void doubleJump() {
		// TODO Auto-generated method stub

	}

	@Override
	public void jump() {
		if (this.wasWayBlocked(Shape.DOWN)) {
			this.setVelocity(new Vector(this.getVelocity().x, -0.25f * 98.1f
					* this.getMass()));
		}
	}

	@Override
	public void run(int direction) {
		switch (direction) {
		case Playable.DIRECTION_LEFT:
			this.setVelocity(new Vector(-100, this.getVelocity().y));
			// Nach links laufen
			break;
		case Playable.DIRECTION_RIGHT:
			this.setVelocity(new Vector(100, this.getVelocity().y));
			// Nach rechts laufen
			break;
		default:
			break;
		}
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("figure-rolling-ball.png"));
	}
}
