/**
 * 
 */
package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Collision;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class GreenSlimeWorm extends EnemyTemplate {

	private boolean divisble;

	public GreenSlimeWorm(World world, Vector position, boolean divisible) {
		super(world, new Rectangle(position, 50.0f, 21.0f), 5.0f, true);
		this.divisble = divisible;
	}

	private GreenSlimeWorm(World world, Vector position) {
		super(world, new Rectangle(position, 30.0f, 13.0f), 5.0f, true);
		this.divisble = false;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.isAlive() == false) {
			this.getWorld().removeFromAllLists(this);
			// Aus allen Listen der Welt löschen, da dieses Objekt besiegt wurde
		}
		this.applyForce(Vector.DOWN.mul(this.getMass() * 9.81f)); // Schwerkarft
		// TODO: Geschwindigkeiten anpassen
		if (this.isWayBlocked(Shape.RIGHT)) {
			this.setVelocity(Vector.LEFT.mul(5.0f));
			// Richtung von Rechts nach Links ändern
		}
		if (this.isWayBlocked(Shape.LEFT)) {
			this.setVelocity(Vector.RIGHT.mul(5.0f));
			// Richtung von Links nach Rechts ändern
		}
		if (this.getVelocity().x == 0.0f && this.isWayBlocked(Shape.DOWN)) {
			this.setVelocity(Vector.RIGHT.mul(5.0f));
		}
	}

	@Override
	public void onLivingCrash(AbstractObject other) {
		// Soldier kann nur den Spieler töten, wenn dieser nicht oberhalb von
		// ihm ist
		if (other instanceof FigureTemplate) {
			Collision col = this.getShape().getCollision(other.getShape(),
					other.isMoveable(), this.isMoveable());
			if (col.isBlocked(Shape.RIGHT) == true || col.isBlocked(Shape.LEFT)
					|| col.isBlocked(Shape.DOWN)) {
				other.kill(this);
			}
		}
	}

	@Override
	public void kill(AbstractObject killer) {
		if (killer instanceof FigureTemplate && divisble) {
			this.getWorld().add(
					new GreenSlimeWorm(this.getWorld(), this.getPosition()
							.modifyX(this.getPosition().x + 25.0f)));
			this.getWorld().add(
					new GreenSlimeWorm(this.getWorld(), this.getPosition()
							.modifyX(this.getPosition().x - 25.0f)));
			this.setAlive(false);
		}
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("green-slime-worm.png"));
	}
}
