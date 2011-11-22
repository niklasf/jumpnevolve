/**
 *
 */
package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.ForegroundDrawable;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.world.Blockable;
import com.googlecode.jumpnevolve.graphics.world.Damageable;
import com.googlecode.jumpnevolve.graphics.world.GravityActing;
import com.googlecode.jumpnevolve.graphics.world.Living;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.NextCollision;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * @author Erik Wagner
 * 
 */
public abstract class Shot extends ObjectTemplate implements Damageable,
		GravityActing, Blockable, ForegroundDrawable {

	private static final long serialVersionUID = 4190924434159617029L;

	private static final float MINIMUM_SQUARE_VELOCITY = Parameter.OBJECTS_SHOT_MINIVEL
			* Parameter.OBJECTS_SHOT_MINIVEL;

	private final Timer livingTime;

	private Timer toLowVel;

	public Shot(World world, NextShape shape, float mass, float livingTime,
			Vector shotDirection, float shotSpeed) {
		super(world, shape, mass, shotDirection.getDirection().mul(shotSpeed));
		this.livingTime = new Timer(livingTime);
		this.livingTime.start();
		this.toLowVel = new Timer(livingTime / 5.0f);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.getVelocity().squareAbs() < MINIMUM_SQUARE_VELOCITY) {
			this.toLowVel.start();
		} else {
			this.toLowVel.stop();
		}
		if (this.livingTime.didFinish() || this.toLowVel.didFinish()) {
			this.getWorld().removeFromWorld(this);
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		this.livingTime.poll(input, secounds);
		this.toLowVel.poll(input, secounds);
	}

	@Override
	public boolean canDamage(NextCollision col) {
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

	public boolean wantBlock(Blockable other) {
		return true;
	}

	public boolean canBeBlockedBy(Blockable other) {
		return !(other instanceof Living);
	}
}
