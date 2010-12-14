package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * 
 * Beschreibung: Ein Soldat der steht
 * 
 * Spezifikationen: blockbar, nicht schiebbar
 * 
 * Bewegungen: Masse = 5; grundsätzlich keine Bewegung; Schwerkraft wirkt
 * 
 * Aggressivitäten: nur gegen den Spieler, wenn der nicht von oben kommt
 * 
 * Immunitäten: Wird nicht von anderen Soldaten getötet
 * 
 * Aktivierung: keine
 * 
 * Deaktivierung: keine
 * 
 * Besonderheiten: kann als Vorlage für andere Gegner benutzt werden
 * 
 * @author Erik Wagner
 * 
 */
public class Soldier extends EnemyTemplate {

	private static final long serialVersionUID = 5378834855856957746L;

	public Soldier(World world, Vector position) {
		super(world, new Rectangle(position, new Vector(20.0f, 20.0f)), 5.0f,
				true);
		// TODO shape durch position erzeugen und super übergeben
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
		// Soldier kann nur den Spieler töten, wenn dieser nicht oberhalb von
		// ihm ist
		if (other instanceof FigureTemplate) {
			if (this.getShape().getCollision(other.getShape(),
					other.isMoveable(), this.isMoveable()).isBlocked(Shape.UP) == false) {
				other.kill(this);
			}
		}
	}

	@Override
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

	@Override
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("simple-foot-soldier.png"));
	}
}
