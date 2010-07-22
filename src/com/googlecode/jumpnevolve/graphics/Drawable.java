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

package com.googlecode.jumpnevolve.graphics;

import org.newdawn.slick.Graphics;

/**
 * Schnittstelle, für alle Objekte, die gezeichnet werden können.
 * 
 * @author Niklas Fiekas
 */
public interface Drawable {
	
	/**
	 * Wird aufgerufen, wenn das Objekt gezeichnet werden soll.
	 * 
	 * @param g Grafikkontext
	 */
	public void draw(Graphics g);
}
