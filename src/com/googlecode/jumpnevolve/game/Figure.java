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

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * 
 * @author Erik Wagner TODO: init-Methode vervollständigen....................
 *         TODO: Ableiten und Beispiel erschaffen
 * 
 */
public abstract class Figure extends AbstractObject {

	public boolean alive;

	/**
	 * @param type
	 * @param position
	 * @param dimension
	 */
	protected Figure(byte type, Vector position, Vector dimension) {
		super(type, position, dimension);
		this.init();
	}

	/**
	 * @param type
	 * @param position
	 * @param dimension
	 * @param force
	 */
	protected Figure(byte type, Vector position, Vector dimension, Vector force) {
		super(type, position, dimension, force);
		this.init();
	}

	private void init() {
		this.alive = true;
		this.setVelocity(Vector.ZERO);
	}

	@Override
	public boolean getState() {
		return this.alive;
	}
}
