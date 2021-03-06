/**
 *
 */
package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.effects.FogEmitterFactory;
import com.googlecode.jumpnevolve.graphics.effects.ParticleEffect;
import com.googlecode.jumpnevolve.graphics.effects.WaterfallEmitterFactory;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.CollisionResult;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Masses;

/**
 * Eine Flüssigkeit, die die Geschwindigkeit der Objekte darin bis zu einer
 * gegebenen maximalen Geschwindigkeit vermindert
 * 
 * @author Erik Wagner
 * 
 *         FIXME: Effekte sollen angezeigt werden (auf ganzer Breite des Fluid)
 * 
 *         FIXME: Objekte werden nicht richtig gebremst
 */
public class Fluid extends ObjectTemplate {

	private static final long serialVersionUID = 8268262299185461488L;

	private final float maximumVelocity;
	private final ParticleEffect effect1;
	private final ParticleEffect effect2;

	/**
	 * @param world
	 * @param shape
	 * @param mass
	 * @param blockable
	 * @param pushable
	 */
	public Fluid(World world, Vector position, Vector dimension,
			float maximumVelocity) {
		super(world, ShapeFactory.createRectangle(position, dimension),
				Masses.NO_MASS);
		this.maximumVelocity = maximumVelocity;
		this.effect1 = new ParticleEffect(position, new FogEmitterFactory());
		this.effect2 = new ParticleEffect(position,
				new WaterfallEmitterFactory(dimension.y * 2));
	}

	public Fluid(World world, Vector position, String arguments) {
		this(world, position, Vector.parseVector(arguments.split(",")[0]),
				Float.parseFloat(arguments.split(",")[1]));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
	}

	@Override
	public void onGeneralCrash(AbstractObject other, CollisionResult colRe) {
		if (other.isMoveable()) {
			float velAbs = other.getVelocity().abs();
			if (velAbs > this.maximumVelocity) {
				other.applyForce(other
						.getVelocity()
						.getDirection()
						.mul((velAbs - maximumVelocity) * 2.0f
								* other.getMass()).neg());
			}
		}
	}

	public void draw(Graphics g) {
		/*
		 * GraphicUtils.texture(g, this.getShape(),
		 * ResourceManager.getInstance() .getImage("textures/water.png"),
		 * false); this.effect1.draw(g); this.effect2.draw(g);
		 */
		super.draw(g);
	}
}
