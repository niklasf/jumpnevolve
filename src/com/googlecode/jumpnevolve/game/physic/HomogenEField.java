package com.googlecode.jumpnevolve.game.physic;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.CollisionResult;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.Vector;

public class HomogenEField extends AbstractObject {

	private final boolean global;
	private final Vector force;

	public HomogenEField(World world, NextShape shape, Vector force,
			boolean global) {
		super(world, shape);
		this.global = global;
		this.force = force;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.global) {
			for (PhysicalObject object : PhysicalObject.getPhysicalObjects()) {
				if (object instanceof Electron) {
					object.applyForce(this.force);
				}
			}
		}
	}

	public void onGeneralCrash(AbstractObject other, CollisionResult colResult) {
		if (!this.global && other instanceof Electron) {
			other.applyForce(this.force);
		}
	}

	public void draw(Graphics g) {
		GraphicUtils.draw(g, this.getShape());
	}
}
