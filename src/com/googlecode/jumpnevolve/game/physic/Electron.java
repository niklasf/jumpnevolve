package com.googlecode.jumpnevolve.game.physic;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

public class Electron extends AbstractObject {

	private static final long serialVersionUID = 1640350044318674935L;

	public Electron(World world, Vector position, Vector velocity) {
		super(world, ShapeFactory.createCircle(position, 10), 1.0f, velocity);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		this.applyForce(Vector.UP.mul(0.2f));
		this.applyForce(this.getVelocity().rotate((float) (Math.PI / 2.0f)));
	}

	/*
	 * public void draw(Graphics g) { GraphicUtils.texture(g, this.getShape(),
	 * ResourceManager.getInstance() .getImage("textures/aluminium.png"), true);
	 * }
	 */
}
