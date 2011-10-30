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
import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine Kameraimplementierung, die die Kamera permanent auf ein Objekt richtet.
 *
 * @author Niklas Fiekas
 */
public class ObjectFocusingCamera implements Camera {

	private static final long serialVersionUID = 1698285102541595661L;

	protected AbstractObject object;

	/**
	 * @param object
	 *            Das Objekt, das zentriert werden soll.
	 */
	public ObjectFocusingCamera(AbstractObject object) {
		this.object = object;
	}

	@Override
	public Vector getPosition() {
		return this.roundPosition(this.object.getPosition(), 1);
	}

	private Vector roundPosition(Vector position, int factor) {
		float x, y;
		x = position.x / factor;
		y = position.y / factor;
		x = Math.round(x) * factor;
		y = Math.round(y) * factor;
		return new Vector(x, y);
	}
}
