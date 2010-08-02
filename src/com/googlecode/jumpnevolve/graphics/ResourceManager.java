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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 * Der ResourceManager läd Ressourcen wie Bilder oder Sounds vom Dateisystem.
 * Wenn mehrmals nach einer Ressource gefragt wird, kann sie jederzeit aus dem
 * Cache geholt werden.
 * 
 * @author Niklas Fiekas
 */
public class ResourceManager {

	private static ResourceManager instance;

	private LinkedList<String> schedule = new LinkedList<String>();

	private HashMap<String, Image> images = new HashMap<String, Image>();

	private HashMap<String, Sound> sounds = new HashMap<String, Sound>();

	private ResourceManager() {

	}

	private String normalizeIdentifier(String identifier) {
		if (!identifier.startsWith("resources/")) {
			return identifier;
		} else {
			throw new ResourceError(
					"Resource identifiers should not start with resources/");
		}
	}

	/**
	 * Fügt Ressourcen in die Wartschleife ein, sodass sie geladen werden,
	 * sobald die Möglichkeit besteht.
	 * 
	 * @param identifier
	 *            Pfad zur Ressource.
	 */
	public void schedule(String identifier) {
		identifier = normalizeIdentifier(identifier);
		if (!this.schedule.contains(identifier)
				&& !this.images.containsKey(identifier)
				&& !this.sounds.containsKey(identifier)) {
			this.schedule.add(identifier);
		}
	}

	private void load(String identifier) {
		try {
			if (identifier.endsWith(".png")) { // Bild laden
				this.images.put(identifier, new Image(identifier));
			} else if (identifier.endsWith(".png?reverse")) { // Umgedrehted
																// Bild laden
				this.images.put(identifier, getImage(
						identifier.substring(0, identifier.indexOf('?')))
						.getFlippedCopy(true, false));
			} else if (identifier.endsWith(".ogg")) { // Sound laden
				this.sounds.put(identifier, new Sound(identifier));
			} else { // Ressourcentyp unbekannt
				throw new ResourceError("Unkown ressource type: " + identifier);
			}
		} catch (SlickException e) {
			throw new ResourceError(e);
		}
	}

	/**
	 * Läd die gewünschte Bildressource vom Dateisystem oder aus dem Cache.
	 * 
	 * @param id
	 *            Pfad zur Bildressource.
	 * @return Das Bild.
	 */
	public Image getImage(String id) {
		id = normalizeIdentifier(id);

		Image image = this.images.get(id);
		if (image == null) {
			load(id);
			return this.images.get(id);
		} else {
			return image;
		}
	}

	/**
	 * Erzeugt ein Bild das in X-Richtung umgedreht wurde oder holt es aus dem
	 * Cache.
	 * 
	 * @param id
	 *            Pfad zur Bildressource.
	 * @return Das umgedrehte Bild.
	 */
	public Image getRevertedImage(String id) {
		id = normalizeIdentifier(id);

		Image image = this.images.get(id + "?reverse");
		if (image == null) {
			load(id + "?reverse");
			return this.images.get(id + "?reverse");
		} else {
			return image;
		}
	}

	/**
	 * Läd eine Sounddatei vom Dateisystem oder aus dem Cache.
	 * 
	 * @param id
	 *            Pfad zur Soundresource.
	 * @return Die Sounddatei.
	 */
	public Sound getSound(String id) {
		id = normalizeIdentifier(id);

		Sound sound = this.sounds.get(id);
		if (sound == null) {
			load(id);
			return this.sounds.get(id);
		} else {
			return sound;
		}
	}

	/**
	 * Arbeitet die Liste aller angeforderten Ressourcen ab und versucht sie zu
	 * laden.
	 */
	public void load() {
		ArrayList<String> loaded = new ArrayList<String>(this.schedule.size());

		// Ressourcen laden
		try {
			for (String id : this.schedule) {
				load(id);
				loaded.add(id);
			}
		} finally {

			// Aus der Warteschleife löschen
			for (String id : loaded) {
				this.schedule.remove(id);
			}
		}
	}

	/**
	 * @return Der von der gesamten Anwendung geteilte ResourceManager.
	 */
	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
		}
		return instance;
	}
}
