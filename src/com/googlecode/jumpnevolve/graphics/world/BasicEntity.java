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

package com.googlecode.jumpnevolve.graphics.world;

import net.phys2d.raw.Body;
import net.phys2d.raw.CollisionEvent;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Eine erweiterbare Implementierung von {@link Entity}.
 * 
 * @author Erik Wagner
 */
public class BasicEntity implements Entity {

	protected SimulatedWorld world;

	protected Body body;

	public void draw(Graphics g) {
		g.draw(this.world.shapeForBody(this.body));
		g.drawOval(-0.1f, -0.1f, 0.2f, 0.2f);
	}

	public void poll(Input input, float secounds) {
	}

	public void init(Body body) {
		this.body = body;
	}

	public void simulatedWorldChanged(SimulatedWorld simulatedWorld) {
		this.world = simulatedWorld;
	}

	public void collisionOccured(CollisionEvent event, Body other) {
	}

	/**
	 * Ein Entity das nichts tut. Um nichts zu tun, muss es auch nicht
	 * initialisiert werden.
	 */
	public static final BasicEntity NULL = new BasicEntity() {
		@Override
		public void draw(Graphics g) {
		}
	};
}
