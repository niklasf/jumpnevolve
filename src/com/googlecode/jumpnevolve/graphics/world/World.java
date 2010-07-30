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

	public static final float ZOOM = 200;

	public static final float TILE_WIDTH = 0.1f;

	abstract public class Tile implements Drawable {

		abstract public void draw(Graphics g);

		abstract public boolean isPassable();
	}

	private Tile[][] tileMap;

	public World(int columns, int rows) {
		// Array erzuegen
		this.tileMap = new Tile[columns][];
		for (int x = 0; x < columns; x++) {
			this.tileMap[x] = new Tile[rows];
		}

		/*
		 * FIXME: sollte doch von Level zu Level unterschiedlich sein, mit
		 * Löchern und so
		 */
		// Boden
		setRectangle(0, 10, 20, 2, new Tile() {
			@Override
			public void draw(Graphics g) {
				g.drawRect(0, 0, TILE_WIDTH, TILE_WIDTH);
			}

			@Override
			public boolean isPassable() {
				return false;
			}

		});
	}

	public int getColumns() {
		return this.tileMap.length;
	}

	public int getRows() {
		return this.tileMap[0].length;
	}

	public void setPosition(int column, int row, Tile value) {
		if (column >= 0 && row >= 0 && column < getColumns() && row < getRows()) {
			this.tileMap[column][row] = value;
		}
	}

	public void setRectangle(int column, int row, int right, int down,
			Tile value) {
		for (int x = column; x < column + right && x < this.tileMap.length; x++) {
			for (int y = row; y < row + down && y < this.tileMap[x].length; y++) {
				if (x >= 0 && y >= 0) {
					this.tileMap[x][y] = value;
				}
			}
		}
	}

	public Tile getPosition(int column, int row) {
		if (column >= 0 && row >= 0 && column < getColumns() && row < getRows()) {
			return this.tileMap[column][row];
		} else {
			return null;
		}
	}

	private ArrayList<Pollable> pollables = new ArrayList<Pollable>();

	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();

	private Camera camera;

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
			if (object instanceof Drawable && !(object instanceof Tile)) {
				if (!this.drawables.contains(object)) {
					this.drawables.add((Drawable) object);
				}
			}
		}
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

		// Tilemap zeichen
		for (int x = 0; x < this.tileMap.length; x++) {
			for (int y = 0; y < this.tileMap[x].length; y++) {
				if (this.tileMap[x][y] != null) {
					g.pushTransform();
					g.translate(x * TILE_WIDTH, y * TILE_WIDTH);
					this.tileMap[x][y].draw(g);
					g.popTransform();
				}
			}
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
