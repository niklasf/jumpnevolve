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

import java.util.ArrayList;
import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.googlecode.jumpnevolve.graphics.AbstractState;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author niklas
 * 
 */
public class World extends AbstractState {

	private Object[][] objectList;

	public static final float ZOOM = 200;

	public final int subareaWidth, subareaHeight;

	public final int horizontalSubareas, verticalSubareas;

	public final int width, height;

	private ArrayList<Pollable> pollables = new ArrayList<Pollable>();

	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();

	private Camera camera;

	public World(int subareaWidth, int subareaHeight, int width, int height) {
		this.subareaWidth = subareaWidth;
		this.subareaHeight = subareaHeight;
		this.width = width;
		this.height = height;
		int horizontalSubareas = (int) Math.ceil((float) width
				/ (float) subareaWidth);
		int verticalSubareas = (int) Math.ceil((float) height
				/ (float) subareaHeight);
		if (verticalSubareas < 6) {
			verticalSubareas = 1;
		}
		this.horizontalSubareas = horizontalSubareas;
		this.verticalSubareas = verticalSubareas;
		this.objectList = new Object[horizontalSubareas][verticalSubareas];
		for (int i = 0; i < objectList.length; i++) {
			for (int j = 0; j < objectList[i].length; j++) {
				objectList[i][j] = new LinkedList<AbstractObject>();
			}
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		for (Pollable pollable : this.pollables) {
			pollable.poll(input, secounds);
		}
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Camera getCamera() {
		return this.camera;
	}

	public void add(Object object) {
		if (object != null) {
			if (object instanceof Pollable) {
				if (!this.pollables.contains(object)) {
					this.pollables.add((Pollable) object);
				}
			}
			if (object instanceof Drawable) {
				if (!this.drawables.contains(object)) {
					this.drawables.add((Drawable) object);
				}
			}
			if (object instanceof AbstractObject) {
				add((AbstractObject) object);
			}
		}
	}

	private void add(AbstractObject object) {
		// TODO: Objekt in entsprechende LinkedLists einfügen
	}

	public void changedPosition(AbstractObject object) {
		// TODO: Objekt in entsprechende LinkedLists einfügen / löschen
	}

	@Override
	public void draw(Graphics g) {
		g.scale(ZOOM, ZOOM);

		// Kameraeinstellung anwenden
		if (this.camera != null) {
			Vector cameraPosition = this.camera.getPosition();
			g.translate(Engine.getInstance().getWidth() / ZOOM / 2.0f
					- cameraPosition.x, Engine.getInstance().getHeight() / ZOOM
					/ 2.0f - cameraPosition.y);
		}

		// Andere Objekte zeichnen
		for (Drawable drawable : this.drawables) {
			drawable.draw(g);
		}
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

	}

}
