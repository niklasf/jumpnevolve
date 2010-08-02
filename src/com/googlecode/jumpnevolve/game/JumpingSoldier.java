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

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.CollisionEvent;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.BasicEntity;

/**
 * Ein Gegner, der auf der Stelle springt.
 * 
 * @author Erik Wagner, Niklas Fiekas
 */
public class JumpingSoldier extends BasicEntity {
	
	public static final float MASS = 50f;
	public static final float WIDTH = 0.5f;
	public static final float HEIGHT = 0.6f;
	
	private boolean couldTryToJump = false;
	
	@Override
	public void draw(Graphics g) {
		g.texture(this.world.shapeForBody(this.body), ResourceManager.getInstance().getImage("simple-foot-soldier.png"), true);
	}

	@Override
	public void poll(Input input, float secounds) {
		boolean canJump = false;

		if (this.couldTryToJump) {
			BodyList touching = this.body.getTouching();
			for (int i = 0; i < touching.size(); i++) {
				if (touching.get(i).getPosition().getY() > this.body
						.getPosition().getY() + 0.2f) {
					canJump = true;
					break;
				}
			}
		}

		if (canJump) {
			this.body.addForce(new Vector2f(0.0f, -30000.0f));
			this.couldTryToJump = false;
		}
	}

	@Override
	public void collisionOccured(CollisionEvent event, Body body) {
		if (event.getPoint().getY() > this.body.getPosition().getY() + 0.1f) {
			this.couldTryToJump = true;
		}
	}

	@Override
	public void init(Body body) {
		super.init(body);
		body.setCanRest(false);
	}
}
