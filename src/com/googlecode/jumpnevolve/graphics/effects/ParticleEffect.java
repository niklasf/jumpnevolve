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

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.ResourceError;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Partikelsystem, das verschiedene Effekte zeichnen und animieren kann.
 * 
 * @author Niklas Fiekas
 */
public class ParticleEffect implements Drawable, Pollable {

	static {
		// Es wird eine Grafikvorlage für einen Partikel benötigt.
		// Die Vorlage ist in der Mitte weiß und wird kreisförmig nach außen
		// hin transparent.
		ResourceManager.getInstance().schedule("particle.png");
	}

	private ParticleSystem system;

	private Vector position;

	private ParticleEmitterFactory factory;

	/**
	 * Erzeugt ein neues Partikelsystem.
	 * 
	 * @param position
	 *            Ortsvektor der Position des Systems.
	 * @param factory
	 *            Implementierung des Effekts.
	 */
	public ParticleEffect(Vector position, ParticleEmitterFactory factory) {
		this.position = position;
		this.factory = factory;
	}

	private void createSystem() {
		if (this.system == null) {
			try {
				this.system = new ParticleSystem(ResourceManager.getInstance()
						.getImage("textures/particle.png"), 200);
				this.system.addEmitter(this.factory.createParticleEmitter());
				this.factory = null;
			} catch (ResourceError e) {
				Log.error(e);
			}
		}
	}

	public void draw(Graphics g) {
		createSystem();
		g.pushTransform();
		g.translate(this.position.x, this.position.y);
		g.scale(1.0f / Engine.getInstance().getCurrentState().getZoomX(),
				1.0f / Engine.getInstance().getCurrentState().getZoomY());
		this.system.render();
		g.popTransform();
	}

	public void poll(Input input, float secounds) {
		createSystem();
		this.system.update((int) (secounds * 1000));
	}

	/**
	 * @return Ein Drawable, das diesen Effekt zeichnet. Der Effekt selbst ist
	 *         Drawable und Pollable. Diese Methode kann verwendet werden, wenn
	 *         er nur als Drawable repräsentiert werden soll.
	 */
	public Drawable getDrawable() {
		return new Drawable() {
			public void draw(Graphics g) {
				ParticleEffect.this.draw(g);
			}
		};
	}

	/**
	 * @return Ein Pollable, das diesen Effekt animiert. Der Effekt selbst ist
	 *         Drawable und Pollable. Diese Methode kann verwendet werden, wenn
	 *         er nur als Pollable repräsentiert werden soll.
	 */
	public Pollable getPollable() {
		return new Pollable() {
			public void poll(Input input, float secounds) {
				ParticleEffect.this.poll(input, secounds);
			}
		};
	}
}
