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

package com.googlecode.jumpnevolve.game;

import java.io.Serializable;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author niklas
 *
 */
public class Level implements Serializable {

	private static final long serialVersionUID = -3874163846250300376L;

	abstract class PollableCamera implements Camera, Pollable {
		
	}
	
	public World createWorld() {
		World world =  new World(15, 20);
		
		world.setCamera(new PollableCamera() {
			Vector pos = Vector.ZERO;
			
			@Override
			public Vector getPosition() {
				return this.pos;
			}

			@Override
			public void poll(Input input, float secounds) {
				this.pos = this.pos.add(new Vector(1, 1).mul(secounds));
			}
		});
		world.add(world.getCamera());
		
		return world;
	}
}
