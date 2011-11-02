package com.googlecode.jumpnevolve.graphics.effects;

import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * @author Erik Wagner
 * 
 */
public class AirFlowEmitterFactory implements ParticleEmitterFactory {

	private final Vector direction;

	/**
	 * 
	 */
	public AirFlowEmitterFactory(Vector direction) {
		this.direction = direction;
	}

	@Override
	public ParticleEmitter createParticleEmitter() {
		return new ParticleEmitterAdapter() {

			private static final int INTERVAL = Parameter.EFFECTS_AIRFLOW_INTERVAL;

			private int timer;

			@Override
			public void update(ParticleSystem system, int delta) {
				this.timer -= delta;
				if (this.timer <= 0) {
					this.timer = INTERVAL;

					Particle p = system.getNewParticle(this, 5000);
					p.setColor(1.0f, 1.0f, 1.0f, 0.0f);
					p.setPosition(
							(float) ((Math.random() - 0.5f) * 50.0f * AirFlowEmitterFactory.this.direction.x),
							(float) ((Math.random() - 0.5f) * 50.0f * AirFlowEmitterFactory.this.direction.y));
					p.setVelocity(
							AirFlowEmitterFactory.this.direction.x * 0.2f,
							AirFlowEmitterFactory.this.direction.y * 0.2f);
					p.setColor(0.5f, 0.5f, 0.5f, 0.05f);
					p.setSize(100.0f);
				}
			}

			public void updateParticle(Particle particle, int delta) {
				particle.adjustVelocity(
						((float) Math.random() - 0.5f) * 0.005f,
						((float) Math.random() - 0.45f) * -0.003f);
			}
		};
	}
}
