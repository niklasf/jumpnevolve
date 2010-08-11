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

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Jeder Zustand, den die Grafikengine einnehmen kann, muss von dieser Klasse
 * abgeleitet werden.
 * 
 * @author Niklas Fiekas
 */
public abstract class AbstractState extends BasicGameState implements Pollable,
		Drawable {

	private static int id = 0;

	private final int ID;

	public AbstractState() {
		this.ID = id++;
	}
	
	public static final float ZOOM = 200;

	@Override
	public final int getID() {
		return this.ID;
	}

	@Override
	public int hashCode() {
		return getID();
	}

	@Override
	public boolean equals(Object object) {
		return object != null && object instanceof AbstractState
				&& hashCode() == object.hashCode();
	}

	@Override
	public final void render(GameContainer container, StateBasedGame game,
			Graphics g) {
		g.pushTransform();
		draw(g);
		g.popTransform();
	}

	@Override
	public final void update(GameContainer container, StateBasedGame game,
			int delta) {
		poll(container.getInput(), delta / 1000.0f);
	}
}
