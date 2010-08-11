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
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.*;

/**
 * @author Erik Wagner TODO: init-Methode vervollständigen
 * 
 *         TODO: Ableiten und Beispiel erschaffen
 * 
 */
public abstract class VorlageGegner extends AbstractObject {

	public boolean alive;

	/**
	 * @param type
	 * @param position
	 * @param dimension
	 * @param force
	 * @param worldOfThis
	 */
	protected VorlageGegner(byte type, Vector position, Vector dimension,
			Vector force, World worldOfThis) {
		super(type, position, dimension, force, worldOfThis);
		this.init();
	}

	/**
	 * @param type
	 * @param position
	 * @param dimension
	 * @param worldOfThis
	 */
	protected VorlageGegner(byte type, Vector position, Vector dimension,
			World worldOfThis) {
		super(type, position, dimension, worldOfThis);
		this.init();
	}

	private void init() {
		this.alive = true;
	}

	@Override
	public boolean getState() {
		return this.alive;
	}
}
