package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.ForegroundDrawable;
import com.googlecode.jumpnevolve.graphics.effects.AirFlowEmitterFactory;
import com.googlecode.jumpnevolve.graphics.effects.ParticleEffect;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.CollisionResult;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Masses;

/**
 * @author Erik Wagner
 * 
 *         TODO: Der Effekt wird noch nicht richtig angezeigt
 */
public class AirFlow extends ObjectTemplate implements Activable,
		ForegroundDrawable {

	private static final long serialVersionUID = 3461423177773856068L;

	private final Vector force;
	private ParticleEffect effect;
	private boolean active;

	public AirFlow(World world, Vector position, Vector dimension,
			Vector direction, float force, boolean active) {
		super(world, ShapeFactory.createRectangle(position, dimension,
				direction.clockWiseAng()), Masses.NO_MASS);
		direction = direction.getDirection();
		this.active = active;
		this.force = direction.mul(GRAVITY * force);

		// Darstellungseffekt erstellen
		this.effect = new ParticleEffect(position.sub(direction.mul(Math
				.abs(dimension.y))), new AirFlowEmitterFactory(direction,
				Math.abs(dimension.y) * 2, Math.abs(dimension.x) * 2, force));
	}

	public AirFlow(World world, Vector position, String arguments) {
		this(world, position, Vector.parseVector(arguments.split(",")[0]),
				Vector.parseVector(arguments.split(",")[1]), Float
						.parseFloat(arguments.split(",")[2]), Boolean
						.parseBoolean(arguments.split(",")[3]));
	}

	@Override
	public void activate(Activating activator) {
		this.active = true;
	}

	@Override
	public void deactivate(Activating deactivator) {
		this.active = false;
	}

	@Override
	public boolean isActivableBy(Activating activator) {
		return activator.getCompany() != COMPANY_PLAYER;
	}

	@Override
	public boolean isDeactivableBy(Activating deactivator) {
		return deactivator.getCompany() != COMPANY_PLAYER;
	}

	@Override
	public boolean isActivated() {
		return this.active;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// Nichts tun
	}

	@Override
	public void onGeneralCrash(AbstractObject other, CollisionResult colResult) {
		if (this.active) {
			other.applyForce(this.force);
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		this.effect.poll(input, secounds);
		super.poll(input, secounds);
	}

	@Override
	public void draw(Graphics g) {
		this.effect.draw(g);
	}

	@Override
	public void drawForEditor(Graphics g) {
		super.draw(g);
	}
}
