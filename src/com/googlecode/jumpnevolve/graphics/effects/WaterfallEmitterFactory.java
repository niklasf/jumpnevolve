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

import com.googlecode.jumpnevolve.graphics.Engine;

/**
 * Erzeugt einen Wasserfalleffekt.
 * 
 * @author Niklas Fiekas
 */
public class WaterfallEmitterFactory implements ParticleEmitterFactory {

	private float verticalSize;

	/**
	 * @param verticalSize
	 *            Wie tief das Wasser fallen soll, bevor es sich auflöst.
	 */
	public WaterfallEmitterFactory(float verticalSize) {
		this.verticalSize = verticalSize;
	}

	@Override
	public ParticleEmitter createParticleEmitter() {
		return new ParticleEmitterAdapter() {
			private static final int INTERVAL = 20;

			private int timer;

			@Override
			public void update(ParticleSystem system, int delta) {
				this.timer -= delta;
				if (this.timer <= 0) {
					this.timer = INTERVAL;

					Particle p = system.getNewParticle(this, 5000);
					p.setColor(0.0f, 0.72f, 0.92f, 0.7f);
					p.setPosition((float) ((Math.random() - 0.5f) * 75.0f), 0);
					p.setSize(120.0f);
					p.setVelocity(0, 0.3f);
				}
			}

			public void updateParticle(Particle particle, int delta) {
				if (particle.getY()
						/ Engine.getInstance().getCurrentState().getZoomY() > WaterfallEmitterFactory.this.verticalSize) {
					particle.kill();
				}
				particle.adjustVelocity(
						((float) Math.random() - 0.5f) * 0.003f, 0);
			}
		};
	}

}
