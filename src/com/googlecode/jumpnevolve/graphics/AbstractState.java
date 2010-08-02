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

import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Jeder Zustand, den die Grafikengine einnehmen kann, muss von dieser Klasse
 * abgeleitet werden.
 * 
 * @author Erik Wagner, Niklas Fiekas
 */
public abstract class AbstractState extends BasicGameState implements Pollable,
		Drawable {
	
	/**
	 * Umrechnungsfaktor zwischen Pixelkoordinaten und den Koordinaten der Welt.
	 */
	public static final float ZOOM = 40f;

	private static int id = 0;

	private final int ID;
	
	private LinkedList<Pollable> pollables = new LinkedList<Pollable>();
	private LinkedList<Drawable> drawables = new LinkedList<Drawable>();

	public AbstractState() {
		this.ID = id++;
		add(this);
	}
	
	public void add(Object object) {
		if(object instanceof Pollable) {
			if(!this.pollables.contains(object)) {
				this.pollables.add((Pollable) object);
			}
		}
		if(object instanceof Drawable) {
			if(!this.drawables.contains(object)) {
				this.drawables.add((Drawable) object);
			}
		}
	}
	
	public void addBackground(Object object) {
		if(object instanceof Pollable) {
			if(!this.pollables.contains(object)) {
				this.pollables.addFirst((Pollable) object);
			}
		}
		if(object instanceof Drawable) {
			if(!this.drawables.contains(object)) {
				this.drawables.addFirst((Drawable) object);
			}
		}
	}

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
		g.translate(Engine.getInstance().getWidth() / 2.0f, Engine.getInstance().getHeight() / 2.0f);
		g.scale(ZOOM, ZOOM);
		
		for(Drawable drawable: this.drawables) {
			drawable.draw(g);
		}
		
		g.resetTransform();
		
		// TODO: Allgemeiner machen
		g.drawString("Bewegung: Links, Rechts   Sprung: Leertaste    Neu anfangen: 0   Beenden: Esc", 10, 30);
	}

	@Override
	public final void update(GameContainer container, StateBasedGame game,
			int delta) {
		for(Pollable pollable: this.pollables) {
			pollable.poll(container.getInput(), delta / 1000.0f);
		}
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) {
		ResourceManager.getInstance().load();
	}
}
