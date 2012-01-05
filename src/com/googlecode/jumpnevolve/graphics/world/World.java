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
import com.googlecode.jumpnevolve.graphics.BackgroundDrawable;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.ForegroundDrawable;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine grundlegende World für die Engine. Sie enthält alle Objekte, die auf dem
 * Bildschirm dargestellt werden sollen.
 * 
 * TODO: Hinzufügen und Entfernen von Objekten prüfen
 * 
 * @author Erik Wagner und Niklas Fiekas
 */
public class World extends AbstractState {

	private ArrayList<LinkedList<AbstractObject>> objectList;

	public final int subareaWidth;

	public final int horizontalSubareas;

	public final int width, height;

	private ArrayList<Pollable> pollables = new ArrayList<Pollable>();

	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();

	private ArrayList<ForegroundDrawable> foregroundDrawables = new ArrayList<ForegroundDrawable>();

	private ArrayList<BackgroundDrawable> backgroundDrawables = new ArrayList<BackgroundDrawable>();

	private ArrayList<AbstractObject> objects = new ArrayList<AbstractObject>();

	private ArrayList<AbstractObject> deletedObjects = new ArrayList<AbstractObject>();

	private Camera camera;

	private boolean screenAlreadyConfigured;

	private boolean polling;

	private ArrayList<Object> addings = new ArrayList<Object>();

	private String background = "landscape-photo.png";

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
		for (Object obj : this.addings) {
			this.add(obj);
		}
		this.addings.clear();
		this.polling = true;
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
		this.polling = false;
	}

	/**
	 * Setzt die Kamera für das World-Objekt
	 * 
	 * @param camera
	 *            Die neue Kamera
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	/**
	 * @return Die Kamera, die aktuell verwendet wird
	 */
	public Camera getCamera() {
		return this.camera;
	}

	/**
	 * Fügt der World ein neues Objekt hinzu. Während gerade die poll-Methode
	 * aller Objekte aufgerufen wird, werden die Objekte zwischen gespeichert
	 * und am Anfang des nächsten poll-Aufrufes des World-Objekts hinzugefügt.
	 * Die Objekte werden nach {@link Pollable}, {@link Drawable} und
	 * {@link AbstractObject} sortiert, für die jeweilige Art werden bei jedem
	 * Frame die entsprechenden Methoden aufgerufen (poll(), draw(),
	 * startRound() und endRound()).
	 * 
	 * @param object
	 *            Das neue Objekt
	 */
	public void add(Object object) {
		if (polling == false) {
			if (object != null) {
				if (object instanceof Pollable) {
					if (!this.pollables.contains(object)) {
						this.pollables.add((Pollable) object);
					}
				}
				if (object instanceof Drawable) {
					if (object instanceof BackgroundDrawable) {
						if (!this.backgroundDrawables.contains(object)) {
							this.backgroundDrawables
									.add((BackgroundDrawable) object);
						}
					} else if (object instanceof ForegroundDrawable) {
						if (!this.foregroundDrawables.contains(object)) {
							this.foregroundDrawables
									.add((ForegroundDrawable) object);
						}
					} else {
						if (!this.drawables.contains(object)) {
							this.drawables.add((Drawable) object);
						}
					}
				}
				if (object instanceof AbstractObject) {
					if (!this.objects.contains(object)) {
						this.objects.add((AbstractObject) object);
						addToObjectList((AbstractObject) object);
						if (object instanceof ObjectGroup) {
							for (AbstractObject obj : ((ObjectGroup) object)
									.getObjects()) {
								this.add(obj);
							}
						}
					}
				}
			}
		} else {
			this.addings.add(object);
		}
	}

	private void addToObjectList(AbstractObject object) {
		int start = object.getStartSubarea(subareaWidth, objectList.size() - 1);
		int end = object.getEndSubarea(subareaWidth, objectList.size() - 1);
		for (int i = start; i <= end; i++) {
			this.objectList.get(i).add(object);
		}
	}

	/**
	 * Wird aufgerufen, wenn ein Objekt seine Position verändert hat
	 * 
	 * @param object
	 *            Das Objekt, dessen Position sich verändert hat
	 */
	public void changedPosition(AbstractObject object) {
		int start = object.getStartSubarea(subareaWidth, objectList.size() - 1);
		int end = object.getEndSubarea(subareaWidth, objectList.size() - 1);
		int oldStart = object.getOldStartSubarea(subareaWidth,
				objectList.size() - 1);
		int oldEnd = object.getOldEndSubarea(subareaWidth,
				objectList.size() - 1);
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

	/**
	 * Entfernt das Objekt beim nächsten poll-Aufruf für das World-Objekt aus
	 * diesem
	 * 
	 * @param object
	 *            Das Objekt, das entfernt werden soll
	 */
	public void removeFromWorld(AbstractObject object) {
		this.deletedObjects.add(object);
	}

	private void removeObject(AbstractObject object) {
		this.pollables.remove(object);
		this.foregroundDrawables.remove(object);
		this.drawables.remove(object);
		this.backgroundDrawables.remove(object);
		this.objects.remove(object);
		/*
		 * int start = (int) (object.getHorizontalStart()) / this.subareaWidth;
		 * int end = (int) (object.getHorizontalEnd()) / this.subareaWidth; if
		 * (start < 0) { System.out.println("Korrektur 0" +
		 * object.getClass().getName()); start = 0; } if (end < 0) {
		 * System.out.println("Korrektur 0" + object.getClass().getName()); end
		 * = 0; }
		 * 
		 * if (start > objectList.size()) { System.out.println("Korrektur high"
		 * + object.getClass().getName()); start = objectList.size(); } if (end
		 * > objectList.size()) { System.out.println("Korrektur high" +
		 * object.getClass().getName()); end = objectList.size(); } for (int i =
		 * start; i <= end; i++) { this.objectList.get(i).remove(object); }
		 */
		for (int i = 0; i < this.objectList.size(); i++) {
			while (this.objectList.get(i).contains(object)) {
				this.objectList.get(i).remove(object);
			}
		}
	}

	/**
	 * Ermittelt alle Objekte die wenigstens eine Subarea mit dem Objekt teilen
	 * (Nachbarn)
	 * 
	 * @param object
	 *            Das Objekt, für das die Nachbarn gesucht werden
	 * @return Die Nachbarn des Objekts
	 */
	public ArrayList<LinkedList<AbstractObject>> getNeighbours(
			AbstractObject object) {
		int start = object.getStartSubarea(subareaWidth, objectList.size() - 1);
		int end = object.getEndSubarea(subareaWidth, objectList.size() - 1);
		ArrayList<LinkedList<AbstractObject>> returns = new ArrayList<LinkedList<AbstractObject>>();
		for (int i = start; i <= end; i++) {
			returns.add(this.objectList.get(i));
		}
		return returns;
	}

	public void setBackground(String imageFile) {
		if (imageFile.equals("default")) {
			imageFile = "landscape.png";
		}
		this.background = imageFile;
	}

	/**
	 * @return Der gesamte Dateipfad aus dem das Hintergrundbild geladen wird
	 */
	public String getBackgroundFile() {
		return "backgrounds/" + background;
	}

	protected void drawBackground(Graphics g) {
		GraphicUtils.drawImage(g, ShapeFactory
				.createRectangle(new Vector(this.width / 2.0f,
						this.height / 2.0f), this.width, this.height),
				ResourceManager.getInstance()
						.getImage(this.getBackgroundFile()));
	}

	/**
	 * Bereitet den Grafikkontext für das Zeichnen vor (Kameraeinstellungen,
	 * Zoom, Hintergrund)
	 * 
	 * @param g
	 *            Der Grafikkontext
	 */
	protected void configScreen(Graphics g) {
		// TODO: Zoom und Kameraeinstellungen prüfen
		g.scale(zoomX, zoomY);

		// Kameraeinstellung anwenden
		if (this.camera != null) {
			Vector cameraPosition = this.camera.getPosition();
			g.translate(Engine.getInstance().getWidth() / zoomX / 2.0f
					- cameraPosition.x, Engine.getInstance().getHeight()
					/ zoomY / 2.0f - cameraPosition.y);
		}
		this.drawBackground(g);
		screenAlreadyConfigured = true;
	}

	@Override
	public void draw(Graphics g) {
		if (!screenAlreadyConfigured) {
			this.configScreen(g);
		}

		// Andere Objekte zeichnen
		for (BackgroundDrawable drawable : this.backgroundDrawables) {
			drawable.draw(g);
		}
		for (Drawable drawable : this.drawables) {
			drawable.draw(g);
		}
		for (ForegroundDrawable drawable : this.foregroundDrawables) {
			drawable.draw(g);
		}
		screenAlreadyConfigured = false;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// Zur Zeit nicht benötigt
	}

}
