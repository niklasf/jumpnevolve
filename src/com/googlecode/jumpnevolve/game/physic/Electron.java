package com.googlecode.jumpnevolve.game.physic;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

public class Electron extends PhysicalObject {

	private static final long serialVersionUID = 1640350044318674935L;

	public Electron(World world, Vector position, Vector velocity) {
		super(world, ShapeFactory.createCircle(position, 10), 1.0f, velocity);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// this.applyForce(this.getVelocity().rotate((float) (Math.PI / 2.0f)));
	}

	public void draw(Graphics g) {
		//GraphicUtils.markPosition(g, this.getPosition(), 1.0f, Color.yellow);
	}
}
