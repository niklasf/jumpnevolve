package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

public class Goal extends ObjectTemplate implements Activable {

	private final Level parent;

	public Goal(Level level, Vector position) {
		super(level, ShapeFactory.createRectangle(position, 60.0f, 63.75f),
				0.0f);
		this.parent = level;
	}

	@Override
	public void activate(Activating activator) {
		this.parent.finish();
	}

	@Override
	public void deactivate(Activating deactivator) {
		// Nichts tun
	}

	@Override
	public boolean isActivableBy(Activating activator) {
		return activator.getCompany() == COMPANY_PLAYER;
	}

	@Override
	public boolean isDeactivableBy(Activating deactivator) {
		return false;
	}

	@Override
	public boolean isActivated() {
		return this.parent.isFinished();
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// Nichts tun
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("object-pictures/goal.png"));
	}

}
