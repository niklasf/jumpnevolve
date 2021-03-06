/**
 *
 */
package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.graphics.world.Blockable;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.util.Masses;

/**
 * @author Erik Wagner
 * 
 */
public abstract class Shooter extends ObjectTemplate implements Activable,
		Blockable {

	private static final long serialVersionUID = -7659273604159649375L;

	private final Timer timeToNextShot;

	protected abstract void shot();

	/**
	 * 
	 * @param world
	 * @param shape
	 * @param shotInterval
	 * @param activated
	 */
	public Shooter(World world, NextShape shape, float shotInterval,
			boolean activated) {
		super(world, shape, Masses.NO_MASS);
		this.timeToNextShot = new Timer(shotInterval);
		if (activated) {
			this.timeToNextShot.start();
		}
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.timeToNextShot.didFinish()) {
			this.shot();
			this.timeToNextShot.start(this.timeToNextShot.getStartingTime());
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		this.timeToNextShot.poll(input, secounds);
	}

	@Override
	public void activate(Activating activator) {
		this.timeToNextShot.start();
	}

	@Override
	public void deactivate(Activating deactivator) {
		this.timeToNextShot.stop();
	}

	@Override
	public boolean isActivableBy(Activating activator) {
		// Kann nicht vom Spieler aktiviert werden
		return activator.getCompany() != COMPANY_PLAYER;
	}

	@Override
	public boolean isActivated() {
		return this.timeToNextShot.isRunning();
	}

	@Override
	public boolean isDeactivableBy(Activating deactivator) {
		// Kann nicht vom Spieler deaktiviert werden
		return deactivator.getCompany() != COMPANY_PLAYER;
	}

	public boolean wantBlock(Blockable other) {
		return true;
	}

	public boolean canBeBlockedBy(Blockable other) {
		return false;
	}
}
