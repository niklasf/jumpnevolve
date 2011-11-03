package com.googlecode.jumpnevolve.graphics.effects;

import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

import com.googlecode.jumpnevolve.util.Parameter;

public class SprayEmitterFactory implements ParticleEmitterFactory {

	public SprayEmitterFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ParticleEmitter createParticleEmitter() {
		return new ParticleEmitterAdapter() {

			private static final int INTERVAL = Parameter.EFFECTS_SPRAY_INTERVAL;

			private int timer;

			@Override
			public void update(ParticleSystem system, int delta) {
				this.timer -= delta;
				if (this.timer <= 0) {
					this.timer = INTERVAL;

					Particle p = system.getNewParticle(this, 5000);
					p.setColor(1.0f, 1.0f, 1.0f, 0.0f);
					p.setPosition((float) (Math.random() - 0.5f) * 150.0f,
							(float) (Math.random() - 0.5f) * 150.0f);
					p.setVelocity((float) (p.getX() / 150.0f) * 0.25f,
							(float) (p.getY() / 150.0f) * 0.25f);
					p.setColor(0.886f, 0.69f, 0.027f, 0.5f);
					p.setSize(30.0f);
				}
			}

			public void updateParticle(Particle particle, int delta) {
				particle.adjustVelocity(
						((float) Math.random() - 0.5f) * 0.002f,
						((float) Math.random() - 0.45f) * 0.002f);
				particle.adjustColor(0, 0, 0, -0.00005f * delta);
			}
		};
	}

}
