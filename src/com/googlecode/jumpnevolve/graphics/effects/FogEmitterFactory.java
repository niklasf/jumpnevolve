/*
 * Copyright (C) 2010 Erik Wagner and Niklas Fiekas
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.jumpnevolve.graphics.effects;

import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

import com.googlecode.jumpnevolve.util.Parameter;

/**
 * Erzeugt einen Nebeleffekt.
 * 
 * @author Niklas Fiekas
 */
public class FogEmitterFactory implements ParticleEmitterFactory {

	@Override
	public ParticleEmitter createParticleEmitter() {
		return new ParticleEmitterAdapter() {

			private static final int INTERVAL = Parameter.EFFECTS_FOG_INTERVAL;

			private int timer;

			@Override
			public void update(ParticleSystem system, int delta) {
				this.timer -= delta;
				if (this.timer <= 0) {
					this.timer = INTERVAL;

					Particle p = system.getNewParticle(this, 5000);
					p.setColor(1.0f, 1.0f, 1.0f, 0.0f);
					p.setPosition((float) ((Math.random() - 0.5f) * 200.0f), 0);
					p.setSize(100.0f);
				}
			}

			public void updateParticle(Particle particle, int delta) {
				particle.adjustVelocity(
						((float) Math.random() - 0.5f) * 0.005f,
						((float) Math.random() - 0.45f) * -0.003f);
				if (particle.getLife() > 2500) {
					particle.adjustColor(0.0f, 0.0f, 0.0f, +0.00010f * delta);
				} else {
					particle.adjustColor(0.0f, 0.0f, 0.0f, -0.00010f * delta);
				}
			}
		};
	}

}
