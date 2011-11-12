package com.googlecode.jumpnevolve.graphics.effects;

import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class AirFlowEmitterFactory implements ParticleEmitterFactory {

	private final Vector direction, rotatedDirection;
	private final Vector distance;
	private final float speed;
	private final float width;

	/**
	 * 
	 */
	public AirFlowEmitterFactory(Vector direction, float distance, float width,
			float speed) {
		this.direction = direction.getDirection();
		this.rotatedDirection = this.direction.rotate((float) (Math.PI / 2));
		Vector rawDist = this.direction.mul(distance);
		rawDist = rawDist.modifyX(Math.abs(rawDist.x));
		rawDist = rawDist.modifyY(Math.abs(rawDist.y));
		this.distance = rawDist;
		this.speed = Math.abs(speed) / 50.0f;
		this.width = width;
	}

	@Override
	public ParticleEmitter createParticleEmitter() {
		return new ParticleEmitterAdapter() {

			private final int INTERVAL = (int) (AirFlowEmitterFactory.this.speed / 0.005f);

			private int timer;

			@Override
			public void update(ParticleSystem system, int delta) {
				this.timer -= delta;
				if (this.timer <= 0) {
					this.timer = INTERVAL;

					Particle p = system.getNewParticle(this, 5000);
					p.setColor(1.0f, 1.0f, 1.0f, 0.0f);
					p.setPosition(
							(float) ((Math.random() - 0.5f)
									* AirFlowEmitterFactory.this.width * AirFlowEmitterFactory.this.rotatedDirection.x),
							(float) ((Math.random() - 0.5f)
									* AirFlowEmitterFactory.this.width * AirFlowEmitterFactory.this.rotatedDirection.y));
					p.setVelocity(AirFlowEmitterFactory.this.direction.x
							* AirFlowEmitterFactory.this.speed,
							AirFlowEmitterFactory.this.direction.y
									* AirFlowEmitterFactory.this.speed);
					p.setColor(0.7f, 0.7f, 0.7f, 0.2f);
					p.setSize(50.0f);
				}
			}

			public void updateParticle(Particle particle, int delta) {
				if (Math.abs(particle.getX()) > AirFlowEmitterFactory.this.distance.x
						|| Math.abs(particle.getY()) > AirFlowEmitterFactory.this.distance.y) {
					particle.kill();
				}
				particle.adjustVelocity(((float) Math.random() - 0.5f)
						* 0.00005f * delta, ((float) Math.random() - 0.5f)
						* 0.00005f * delta);
			}
		};
	}
}
