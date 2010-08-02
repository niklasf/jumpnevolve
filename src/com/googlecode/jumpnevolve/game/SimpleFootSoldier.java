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
import net.phys2d.raw.CollisionEvent;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.BasicEntity;

/**
 * @author niklas
 *
 */
public class SimpleFootSoldier extends BasicEntity {
	
	public static final float MASS = 50f;
	public static final float WIDTH = 0.5f;
	public static final float HEIGHT = 0.6f;
	
	public static final float TARGET_VELOCITY = 2.0f;
	
	private short dir;
	
	public static final short LEFT = -1;
	public static final short RIGHT = 1;
	
	private Float leftBorder = null;
	private Float rightBorder = null;
	
	public SimpleFootSoldier() {
		this(RIGHT);
	}
	
	public SimpleFootSoldier(short dir) {
		this.dir = (short) -dir;
	}
	
	public SimpleFootSoldier(short dir, Float leftBorder, Float rightBorder) {
		this.dir = (short) -dir;
		if(leftBorder != null && rightBorder != null && leftBorder >= rightBorder) {
			throw new IllegalArgumentException("Left border must be left of the right border.");
		}
		this.leftBorder = leftBorder;
		this.rightBorder = rightBorder;
	}
	
	@Override
	public void draw(Graphics g) {
		if(this.dir == LEFT) {
			g.texture(this.world.shapeForBody(this.body), ResourceManager.getInstance().getImage("simple-foot-soldier.png"), true);
		} else {
			g.texture(this.world.shapeForBody(this.body), ResourceManager.getInstance().getRevertedImage("simple-foot-soldier.png"), true);
		}
	}
	
	@Override
	public void poll(Input input, float secounds) {
		if(this.leftBorder != null && this.body.getPosition().getX() <= this.leftBorder) {
			this.dir = RIGHT;
		}
		if(this.rightBorder != null && this.body.getPosition().getX() >= this.rightBorder) {
			this.dir = LEFT;
		}
		if(this.body.getVelocity().getX() * this.dir < TARGET_VELOCITY) {
			this.body.addForce(new Vector2f(240.0f * this.dir, 0.0f));
		}
	}
	
	@Override
	public void collisionOccured(CollisionEvent event, Body body) {
		this.dir = (short) (this.dir * -1);
	}
	
	@Override
	public void init(Body body) {
		super.init(body);
		body.setCanRest(false);
		// body.setRotatable(true);
	}
}
