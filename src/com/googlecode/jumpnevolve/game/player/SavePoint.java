/**
 * 
 */
package com.googlecode.jumpnevolve.game.player;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class SavePoint extends AbstractObject implements Activable {

	private final Player parent;
	private boolean activated = false;

	/**
	 * @param world
	 * @param shape
	 */
	SavePoint(World world, Player parent, Vector position) {
		super(world, ShapeFactory.createRectangle(position, 30.0f, 50.0f));
		this.parent = parent;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
	}

	@Override
	public void activate(Activating activator) {
		this.parent.setActivSavepoint(this.getPosition());
		this.activated = true;
		System.out.println("SavePoint activated at " + this.getPosition());
	}

	@Override
	public void deactivate(Activating deactivator) {
		// Kann nicht deaktiviert werden
	}

	@Override
	public boolean isActivableBy(Activating activator) {
		return activator.getCompany() == COMPANY_PLAYER;
	}

	@Override
	public boolean isActivated() {
		return this.activated;
	}

	@Override
	public boolean isDeactivableBy(Activating deactivator) {
		return false;
	}

	@Override
	public int getCompany() {
		return COMPANY_OBJECT;
	}

}
