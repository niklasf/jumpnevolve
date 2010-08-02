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

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;

import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.BasicEntity;

/**
 * Eine Plattform die nach unten fährt, wenn sie mit Gewicht belastet wird
 * und nach oben fährt, wenn sie nicht belastet wird.
 * 
 * @author Niklas Fiekas
 */
public class VerticalSlider extends BasicEntity {
	
	public static final float MASS = 1.0f;
	
	private float upperBorder;
	private float lowerBorder;
	
	/**
	 * @param upperBorder Obere Grenze
	 * @param lowerBorder Untere Grenze
	 */
	public VerticalSlider(float upperBorder, float lowerBorder) {
		this.upperBorder = upperBorder;
		this.lowerBorder = lowerBorder;
	}
	
	@Override
	public void draw(Graphics g) {
		g.texture(this.world.shapeForBody(this.body), ResourceManager.getInstance().getImage("aluminium.png"), 0.5f, 0.5f);
	}
	
	@Override
	public void poll(Input input, float secounds) {
		this.body.setMaxVelocity(0, 1.0f);
		if(this.body.getPosition().getY() < this.upperBorder) {
			this.body.setMaxVelocity(0.0f, 1.0f);
			this.body.addForce(new Vector2f(0.0f, 10.0f));
		} else {
			this.body.addForce(new Vector2f(0.0f, -20.0f));
		}
		
		if(this.body.getPosition().getY() > this.lowerBorder) {
			this.body.setMaxVelocity(0, 100.f);
			this.body.addForce(new Vector2f(0.0f, -1000.0f));
		}
	}
	
	@Override
	public void init(Body body) {
		super.init(body);
		body.setCanRest(false);
		body.setGravityEffected(false);
		body.setMaxVelocity(0, 1.0f);
	}
}
