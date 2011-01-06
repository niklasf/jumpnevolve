/**
 * 
 */
package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.player.PlayerFigure;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Damageable;
import com.googlecode.jumpnevolve.graphics.world.Living;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Collision;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * 
 * Beschreibung: Ein Schleim der nach rechts und links läuft
 * 
 * Spezifikationen: blockbar, nicht schiebbar
 * 
 * Bewegungen: Schwerkraft wirkt; bewegt sich nach rechts und links und prallt
 * von Wänden ab; Geschwindigkeit: 10 Pixel pro Sekunde
 * 
 * Aggressivitäten: nur gegen den Spieler, der von rechts/links/unten kommt
 * 
 * Immunitäten: kann nur getötet werden, nachdem er beim ersten Mal gespalten
 * wurde
 * 
 * Aktivierung: keine
 * 
 * Deaktivierung: keine
 * 
 * Besonderheiten: teilt sich beim ersten Mal daraufspringen
 * 
 * @author Erik Wagner
 * 
 */
public class GreenSlimeWorm extends EnemyTemplate {

	private boolean divisble;

	public GreenSlimeWorm(World world, Vector position) {
		super(world, new Rectangle(position, 80.0f, 21.0f), 5.0f, true);
		this.divisble = true;
	}

	private GreenSlimeWorm(World world, Vector position, boolean next) {
		super(world, new Rectangle(position, 50.0f, 13.0f), 5.0f, true);
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
	public void draw(Graphics g) {
		GraphicUtils
				.drawImage(g, this.getShape(), ResourceManager.getInstance()
						.getImage("object-pictures/green-slime-worm.png"));
	}

	@Override
	public int getDamage() {
		return 1;
	}

	@Override
	public int getKindOfDamage() {
		return DAMAGE_NORMAL;
	}

	@Override
	public boolean wantDamaging(Living object) {
		return object.getCompany() == COMPANY_PLAYER;
	}

	@Override
	public void damage(Damageable damager) {
		this.killed();
	}

	@Override
	public int getDeff(int kindOfDamage) {
		return 0;
	}

	@Override
	public int getHP() {
		return 1;
	}

	@Override
	public void killed() {
		if (divisble) {
			this.getWorld().add(
					new GreenSlimeWorm(this.getWorld(), this.getPosition()
							.modifyX(this.getPosition().x + 25.0f), true));
			this.getWorld().add(
					new GreenSlimeWorm(this.getWorld(), this.getPosition()
							.modifyX(this.getPosition().x - 25.0f), true));
		}
		this.setAlive(false);
	}

	@Override
	public boolean canDamage(Collision col) {
		return col.isBlocked(Shape.RIGHT) || col.isBlocked(Shape.LEFT)
				|| col.isBlocked(Shape.DOWN);
	}
}
