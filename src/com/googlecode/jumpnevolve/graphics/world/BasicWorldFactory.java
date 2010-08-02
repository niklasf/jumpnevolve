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

import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;

/**
 * @author niklas
 *
 */
public class BasicWorldFactory implements WorldFactory {

	public static final int ITERATIONS = 10;
	
	private World world;
	
	public BasicWorldFactory() {
		this(new Vector2f(0, 10.0f));
	}
	
	public BasicWorldFactory(Vector2f gravity) {
		this.world = new World(gravity, ITERATIONS);
		this.world.enableRestingBodyDetection(0.1f, 0.1f, 0.1f);
	}
	
	private Vector2f position = new Vector2f(0, 0);
	
	private float mass = 1.0f;
	
	public ROVector2f setPosition(ROVector2f position) {
		Vector2f oldPosition = this.position;
		this.position = new Vector2f(position);
		return oldPosition;
	}
	
	public float setMass(float mass) {
		float oldMass = this.mass;
		this.mass = mass;
		return oldMass;
	}
	
	public Body addAxisAlignedBox(Entity entity, float width, float height) {
		Body body = new Body(new Box(width, height), this.mass);
		body.setPosition(this.position.getX(), this.position.getY());
		body.setRotatable(false);
		return init(entity, body);
	}
	
	public Body addBall(Entity entity, float radius) {
		Body body = new Body(new Circle(radius), this.mass);
		body.setPosition(this.position.getX(), this.position.getY());
		return init(entity, body);
	}
	
	public Body addAxisAlignedWall(Entity entity, float startX, float startY, float x, float y) {
		Body body = new StaticBody(new Box(x, y));
		body.setPosition(startX + x / 2.0f, startY + y / 2.0f);
		return init(entity, body);
	}
	
	public Body addAxisAlignedWall(Entity entity, float width, float height) {
		StaticBody body = new StaticBody(new Box(width, height));
		body.setPosition(this.position.getX(), this.position.getY());
		return init(entity, body);
	}

	protected Body init(Entity entity, Body body) {
		body.setUserData(entity);
		this.world.add(body);
		if(entity != null) {
			entity.init(body);
		}
		return body;
	}
	
	public World getWorld() {
		return this.world;
	}

}
