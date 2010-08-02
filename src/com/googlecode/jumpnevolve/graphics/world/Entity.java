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

import net.phys2d.raw.Body;
import net.phys2d.raw.CollisionEvent;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;

/**
 * Repräsentiert ein Objekt aus der Simulation im restlichen Programm. Bevor
 * Methoden aus {@link Pollable} und {@link Drawable} aufgerufen werden, sind
 * zumächst {@link #init(Body)} und
 * {@link #simulatedWorldChanged(SimulatedWorld)} aufgerufen worden.
 * 
 * @author Erik Wagner
 */
public interface Entity extends Pollable, Drawable {

	/**
	 * Sobald das physikalische Objekt erzeugt wurde, wird das dem Entity Objekt
	 * mitgeteilt.
	 * 
	 * @param body
	 *            Physikalisches Objekt.
	 */
	public void init(Body body);

	/**
	 * Wird aufgerufen, wenn eine Kollision vorliegt.
	 * 
	 * @param event
	 *            Weitere Informationen zum Konflikt und die vorerst verwendete
	 *            Lösung.
	 * @param other
	 *            Das andere, an der Kollision beteiligte, physikalische Objekt.
	 */
	public void collisionOccured(CollisionEvent event, Body other);

	public void simulatedWorldChanged(SimulatedWorld simulatedWorld);
}
