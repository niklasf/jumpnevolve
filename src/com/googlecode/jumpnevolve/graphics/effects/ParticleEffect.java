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

import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.graphics.AbstractState;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.ResourceError;
import com.googlecode.jumpnevolve.graphics.ResourceManager;

/**
 * @author niklas
 *
 */
public class ParticleEffect implements Drawable, Pollable {

	static {
		ResourceManager.getInstance().schedule("particle.png");
	}

	private ParticleSystem system;
	
	private Vector2f position;
	
	private ParticleEmitterFactory factory;
	
	public ParticleEffect(ROVector2f position, ParticleEmitterFactory factory) {
		this.position = new Vector2f(position);
		this.factory = factory;
	}
	
	private void createSystem() {
		if(this.system == null) {
			try {
				this.system = new ParticleSystem(ResourceManager.getInstance().getImage("particle.png"), 200);
				this.system.addEmitter(this.factory.createParticleEmitter());
				this.factory = null;
			} catch(ResourceError e) {
				Log.error(e);
			}
		}
	}

	public void draw(Graphics g) {
		createSystem();
		g.pushTransform();
		g.translate(this.position.getX(), this.position.getY());
		g.scale(1.0f / AbstractState.ZOOM, 1.0f / AbstractState.ZOOM);
		this.system.render();
		g.popTransform();
	}

	public void poll(Input input, float secounds) {
		createSystem();
		this.system.update((int) (secounds * 1000));
	}

	public Drawable getDrawable() {
		return new Drawable() {
			public void draw(Graphics g) {
				ParticleEffect.this.draw(g);
			}
		};
	}
	
	public Pollable getPollable() {
		return new Pollable() {
			public void poll(Input input, float secounds) {
				ParticleEffect.this.poll(input, secounds);
			}
		};
	}
}
