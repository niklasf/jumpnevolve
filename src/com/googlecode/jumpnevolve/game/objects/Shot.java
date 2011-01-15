/**
 * 
 */
package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.world.Damageable;
import com.googlecode.jumpnevolve.graphics.world.GravityActing;
import com.googlecode.jumpnevolve.graphics.world.Living;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Collision;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public abstract class Shot extends ObjectTemplate implements Damageable,
		GravityActing {

	private final Timer livingTime;

	/**
	 * @param world
	 * @param shape
	 * @param mass
	 * @param blockable
	 * @param pushable
	 */
	public Shot(World world, Shape shape, float livingTime,
			Vector shotDirection, float shotSpeed) {
		super(world, shape, 0.1f, true, shotDirection.getDirection().mul(
				shotSpeed));
		this.livingTime = new Timer(livingTime);
		this.livingTime.start();
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.livingTime.didFinish()) {
			this.getWorld().removeFromAllLists(this);
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		this.livingTime.poll(input, secounds);
	}

	@Override
	public boolean canDamage(Collision col) {
		// Kann immmer beschädigen
		return true;
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
		// Beschädigt jedes Objekt, mit dem es zusammentrifft
		return true;
	}
}