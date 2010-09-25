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
 * TODO: Klassenbeschreibung TODO: Dokumentation TODO: Hinzufügen und entfernen
 * von Objekten prüfen
 * 
 * @author Erik Wagner und Niklas Fiekas
 */
public class World extends AbstractState{

	private ArrayList<LinkedList<AbstractObject>> objectList;

	public final int subareaWidth;

	public final int horizontalSubareas;

	public final int width, height;

	private ArrayList<Pollable> pollables = new ArrayList<Pollable>();

	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();

	private ArrayList<AbstractObject> objects = new ArrayList<AbstractObject>();

	private ArrayList<AbstractObject> deletedObjects = new ArrayList<AbstractObject>();

	private Camera camera;

	public World(int width, int height, int subareaWidth) {
		this.subareaWidth = subareaWidth;
		this.width = width;
		this.height = height;
		this.horizontalSubareas = (int) Math.ceil((float) width
				/ (float) subareaWidth);
		this.objectList = new ArrayList<LinkedList<AbstractObject>>(
				this.horizontalSubareas);
		for (int i = 0; i < this.horizontalSubareas; i++) {
			this.objectList.add(new LinkedList<AbstractObject>());
		}

	}

	@Override
	public void poll(Input input, float secounds) {
		this.deletedObjects.clear();
		for (AbstractObject object : this.objects) {
			object.startRound(input);
		}
		for (AbstractObject object : this.deletedObjects) {
			this.removeObject(object);
		}
		for (Pollable pollable : this.pollables) {
			pollable.poll(input, secounds);
		}
		for (AbstractObject object : this.objects) {
			object.endRound();
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
				if (!this.objects.contains(object)) {
					this.objects.add((AbstractObject) object);
					addToObjectList((AbstractObject) object);
				}
			}
		}
	}

	private void addToObjectList(AbstractObject object) {
		int start = (int) (object.getHorizontalStart()) / this.subareaWidth;
		int end = (int) (object.getHorizontalEnd()) / this.subareaWidth;
		if (start < 0) {
			System.out.println("Korrektur 0" + object.getClass().getName());
			start = 0;
		}
		if (end < 0) {
			System.out.println("Korrektur 0" + object.getClass().getName());
			end = 0;
		}

		if (start > objectList.size()) {
			System.out.println("Korrektur high" + object.getClass().getName());
			start = objectList.size();
		}
		if (end > objectList.size()) {
			System.out.println("Korrektur high" + object.getClass().getName());
			end = objectList.size();
		}
		for (int i = start; i <= end; i++) {
			this.objectList.get(i).add(object);
		}
	}

	public void changedPosition(AbstractObject object) {
		int start = (int) (object.getHorizontalStart()) / this.subareaWidth;
		int end = (int) (object.getHorizontalEnd()) / this.subareaWidth;
		int oldStart = (int) (object.getOldHorizontalStart()) / this.subareaWidth;
		int oldEnd = (int) (object.getOldHorizontalEnd()) / this.subareaWidth;
		if (start < 0) {
			System.out.println("Korrektur 0" + object.getClass().getName());
			start = 0;
		}
		if (oldStart < 0) {
			System.out.println("Korrektur 0" + object.getClass().getName());
			oldStart = 0;
		}
		if (end < 0) {
			System.out.println("Korrektur 0" + object.getClass().getName());
			end = 0;
		}
		if (oldEnd < 0) {
			System.out.println("Korrektur 0" + object.getClass().getName());
			oldEnd = 0;
		}

		if (start > objectList.size()) {
			System.out.println("Korrektur high" + object.getClass().getName());
			start = objectList.size();
		}
		if (oldStart > objectList.size()) {
			System.out.println("Korrektur high" + object.getClass().getName());
			oldStart = objectList.size();
		}
		if (end > objectList.size()) {
			System.out.println("Korrektur high" + object.getClass().getName());
			end = objectList.size();
		}
		if (oldEnd > objectList.size()) {
			System.out.println("Korrektur high" + object.getClass().getName());
			oldEnd = objectList.size();
		}
		if (start < oldStart) {
			for (int i = start; i < oldStart; i++) {
				this.objectList.get(i).add(object);
			}
		}
		if (start > oldStart) {
			for (int i = oldStart; i < start; i++) {
				this.objectList.get(i).remove(object);
			}
		}
		if (end > oldEnd) {
			for (int i = oldEnd + 1; i <= end; i++) {
				this.objectList.get(i).add(object);
			}
		}
		if (end < oldEnd) {
			for (int i = end + 1; i <= oldEnd; i++) {
				this.objectList.get(i).remove(object);
			}
		}
	}

	public void removeFromAllLists(AbstractObject object) {
		this.deletedObjects.add(object);
	}

	private void removeObject(AbstractObject object) {
		this.pollables.remove(object);
		this.drawables.remove(object);
		this.objects.remove(object);
		int start = (int) (object.getHorizontalStart()) / this.subareaWidth;
		int end = (int) (object.getHorizontalEnd()) / this.subareaWidth;
		if (start < 0) {
			System.out.println("Korrektur 0" + object.getClass().getName());
			start = 0;
		}
		if (end < 0) {
			System.out.println("Korrektur 0" + object.getClass().getName());
			end = 0;
		}

		if (start > objectList.size()) {
			System.out.println("Korrektur high" + object.getClass().getName());
			start = objectList.size();
		}
		if (end > objectList.size()) {
			System.out.println("Korrektur high" + object.getClass().getName());
			end = objectList.size();
		}
		for (int i = start; i <= end; i++) {
			this.objectList.get(i).remove(object);
		}
	}

	public ArrayList<LinkedList<AbstractObject>> getNeighbours(
			AbstractObject object) {
		int start = (int) (object.getHorizontalStart()) / this.subareaWidth;
		int end = (int) (object.getHorizontalEnd()) / this.subareaWidth;
		if (start < 0) {
			System.out.println("Korrektur 0" + object.getClass().getName());
			start = 0;
		}
		if (end < 0) {
			System.out.println("Korrektur 0" + object.getClass().getName());
			end = 0;
		}

		if (start > objectList.size()) {
			System.out.println("Korrektur high" + object.getClass().getName());
			start = objectList.size();
		}
		if (end > objectList.size()) {
			System.out.println("Korrektur high" + object.getClass().getName());
			end = objectList.size();
		}
		ArrayList<LinkedList<AbstractObject>> returns = new ArrayList<LinkedList<AbstractObject>>();
		for (int i = start; i <= end; i++) {
			returns.add(this.objectList.get(i));
		}
		return returns;
	}

	@Override
	public void draw(Graphics g) {
		// TODO: Zoom und Kameraeinstellungen prüfen
		g.scale(zoomX, zoomY);

		// Kameraeinstellung anwenden
		if (this.camera != null) {
			Vector cameraPosition = this.camera.getPosition();
			g.translate(Engine.getInstance().getWidth() / zoomX / 2.0f
					- cameraPosition.x, Engine.getInstance().getHeight() / zoomY
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
