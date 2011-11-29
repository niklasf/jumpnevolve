package com.googlecode.jumpnevolve.game.player;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Masses;

/**
 * @author Erik Wagner
 * 
 */
public class SavePoint extends ObjectTemplate implements Activable {

	private static final long serialVersionUID = 7338696105370871642L;

	private final Player parent;
	private boolean activated = false;

	public SavePoint(World world, Vector position, Player parent) {
		super(world, ShapeFactory.createRectangle(position, 30.0f, 45.0f),
				Masses.NO_MASS);
		this.parent = parent;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
	}

	@Override
	public void activate(Activating activator) {
		this.parent.setActivSavepoint(this.getPosition());
		this.activated = true;
		Log.info("SavePoint activated at " + this.getPosition());
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
	public void draw(Graphics g) {
		if (this.activated) {
			GraphicUtils.drawImage(
					g,
					this.getShape(),
					ResourceManager.getInstance().getImage(
							"object-pictures/savePoint-active.png"));
		} else {
			GraphicUtils.drawImage(
					g,
					this.getShape(),
					ResourceManager.getInstance().getImage(
							"object-pictures/savePoint-not-active.png"));

		}
	}

}
