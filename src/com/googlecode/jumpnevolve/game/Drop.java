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

import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;

import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.BasicEntity;

/**
 * @author niklas
 *
 */
public class Drop extends BasicEntity {
	
	public static final float MASS = 100.0f;
	
	public static final float RADIUS = 0.3f;
	
	private static final Random doom = new Random();
	
	private Vector2f position;
	
	@Override
	public void draw(Graphics g) {
		g.texture(this.world.shapeForBody(this.body), ResourceManager.getInstance().getImage("water-surface.png"), 0.5f, 0.5f);
	}
	
	@Override
	public void poll(Input input, float secounds) {
		if(this.body.getPosition().getY() > 20.0f || this.body.disabled()) {
			this.body.adjustVelocity(new Vector2f(- this.body.getVelocity().getX(), -this.body.getVelocity().getY()));
			this.body.adjustAngularVelocity((float) (- this.body.getAngularVelocity() + (doom.nextDouble() - 0.5f) * 100.0f));
			this.body.setPosition(this.position.getX(), this.position.getY());
		}
	}
	
	@Override
	public void init(Body body) {
		super.init(body);
		body.setCanRest(false);
		body.setMaxVelocity(15.0f, 15.0f);
		body.adjustAngularVelocity(-200.0f);
		this.position = new Vector2f(body.getPosition());
	}
}
