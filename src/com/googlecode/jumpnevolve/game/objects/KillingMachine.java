package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.player.PlayerFigure;
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
	public boolean canDamage(Collision col) {
		return !col.isBlocked(Shape.UP);
	}

	@Override
	public int getDamage() {
		return 10;
	}

	@Override
	public int getKindOfDamage() {
		return DAMAGE_NORMAL;
	}

	@Override
	public boolean wantDamaging(Living object) {
		return true;
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

	// TODO: draw-Methode einfügen

}
