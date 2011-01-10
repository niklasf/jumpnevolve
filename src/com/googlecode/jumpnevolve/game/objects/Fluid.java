/**
 * 
 */
package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.effects.FogEmitterFactory;
import com.googlecode.jumpnevolve.graphics.effects.ParticleEffect;
import com.googlecode.jumpnevolve.graphics.effects.WaterfallEmitterFactory;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

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
		super(world, new Rectangle(position, dimension), 0.0f, false, false);
		this.maximumVelocity = maximumVelocity;
		this.effect1 = new ParticleEffect(position, new FogEmitterFactory());
		this.effect2 = new ParticleEffect(position,
				new WaterfallEmitterFactory(dimension.y * 2));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
	}

	@Override
	public void onGeneralCrash(AbstractObject other) {
		if (other.isMoveable()) {
			float velAbs = other.getVelocity().abs();
			// System.out.println("Moveable-Crash: " + velAbs + " MaxVel: "
			// + maximumVelocity);
			if (velAbs > this.maximumVelocity) {
				other.applyForce(other.getVelocity().getDirection().mul(
						(velAbs - maximumVelocity) * 0.7f));
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
