package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.graphics.world.Blockable;
import com.googlecode.jumpnevolve.graphics.world.Damageable;
import com.googlecode.jumpnevolve.graphics.world.GravityActing;
import com.googlecode.jumpnevolve.graphics.world.Living;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.NextCollision;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
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
public abstract class KillingMachine extends EnemyTemplate implements
		GravityActing, Blockable {

	private static final long serialVersionUID = -5724600752326575341L;

	public KillingMachine(World world, Vector position) {
		super(world, ShapeFactory.createCircle(position, 15.0f), 20.0f);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.isAlive() == false) {
			this.getWorld().removeFromWorld(this);
			// Aus allen Listen der Welt löschen, da dieses Objekt besiegt wurde
		}
	}

	@Override
	public boolean canDamage(NextCollision col) {
		return col.isBlocked(Shape.DOWN) || col.isBlocked(Shape.RIGHT)
				|| col.isBlocked(Shape.LEFT);
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

	@Override
	public boolean canBeBlockedBy(Blockable other) {
		return other.getCompany() != COMPANY_PLAYER;
	}

	@Override
	public boolean wantBlock(Blockable other) {
		return true;
	}
}
