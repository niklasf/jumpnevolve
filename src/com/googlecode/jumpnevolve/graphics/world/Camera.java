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

import java.io.Serializable;

import com.googlecode.jumpnevolve.math.Vector;

/**
 * Wird implementiert um Kameraeinstellungen zu erstellen.
 * 
 * @author Niklas Fiekas
 */
public interface Camera extends Serializable {

	/**
	 * @return Gibt den Ortsvektor des Mittelpunkts des gewünschten Blickfeldes
	 *         zurück.
	 */
	public Vector getPosition();
}
