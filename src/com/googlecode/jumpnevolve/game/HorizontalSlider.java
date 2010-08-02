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

import net.phys2d.raw.Body;

import org.newdawn.slick.Input;

/**
 * @author niklas
 *
 */
public class HorizontalSlider extends Wood {
	
	public static final short LEFT = -1;
	public static final short RIGHT = 1;
	
	public static final float MASS = 1.0f;
	
	private short dir;
	
	private float leftBorder;
	private float rightBorder;
	
	public HorizontalSlider(short dir, float leftBorder, float rightBorder) {
		this.dir = dir;
		this.leftBorder = leftBorder;
		this.rightBorder = rightBorder;
	}
	
	@Override
	public void poll(Input input, float secounds) {
		if(this.body.getPosition().getX() < this.leftBorder) {
			this.dir = RIGHT;
		}
		if(this.body.getPosition().getX() > this.rightBorder) {
			this.dir = LEFT;
		}
		this.body.setPosition(this.body.getPosition().getX() + this.dir * secounds * 0.0004f, this.body.getPosition().getY());
	}
	
	@Override
	public void init(Body body) {
		super.init(body);
		body.setCanRest(false);
		body.setMaxVelocity(1.0f, 0);
	}
}
