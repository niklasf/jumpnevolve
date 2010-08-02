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

import com.googlecode.jumpnevolve.graphics.Pollable;

/**
 * Schnittstelle für eine Kameraeinstellung. Die Kamera bestimmt, welche
 * Objekte oder Punkte im Bildmittelpunkt liegen.
 * 
 * @author Niklas Fiekas
 */
public interface Camera extends Pollable {
	
	/**
	 * @return Der Ortsvektor des gewünschten Bildmittelpunkts.
	 */
	public ROVector2f getPosition();
}
