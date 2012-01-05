package com.googlecode.jumpnevolve.game.physic;


import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.CollisionResult;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.Vector;

public class HomogenBField extends AbstractObject {
	private final boolean global;
	private final float force;

	public HomogenBField(World world, NextShape shape, float forceFactor,
			boolean global) {
		super(world, shape);
		this.global = global;
		this.force = forceFactor;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.global) {
			for (PhysicalObject object : PhysicalObject.getPhysicalObjects()) {
				if (object instanceof Electron) {
					object.applyForce(object.getVelocity()
							.rotate((float) (Math.PI / 2.0f)).mul(this.force));
				}
			}
		}
	}

	public void onGeneralCrash(AbstractObject other, CollisionResult colResult) {
		if (!this.global && other instanceof Electron) {
			other.applyForce(other.getVelocity()
					.rotate((float) (Math.PI / 2.0f)).mul(this.force));
		}
	}
	
	public void draw(Graphics g) {
		GraphicUtils.draw(g, this.getShape());
	}
}