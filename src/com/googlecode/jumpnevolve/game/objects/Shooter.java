/**
 * 
 */
package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

/**
 * @author Erik Wagner
 * 
 */
public abstract class Shooter extends ObjectTemplate implements Activable {

	private final Timer timeToNextShot;

	protected abstract void shot();

	/**
	 * @param world
	 * @param shape
	 * @param mass
	 * @param blockable
	 * @param pushable
	 */
	public Shooter(World world, Shape shape, float shotInterval,
			boolean activated) {
		super(world, shape, 0.0f, true);
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
}
