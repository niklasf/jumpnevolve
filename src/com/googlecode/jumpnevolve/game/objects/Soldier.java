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
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage(
						"object-pictures/simple-foot-soldier.png"));
	}

	@Override
	public boolean canDamage(Collision col) {
		return col.isBlocked(Shape.DOWN) || col.isBlocked(Shape.RIGHT)
				|| col.isBlocked(Shape.LEFT);
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
		this.setAlive(false);
	}
}
