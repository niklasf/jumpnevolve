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

import java.util.HashMap;
import java.util.LinkedList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * @author niklas
 *
 */
public class ResourceManager {

	private static ResourceManager instance;
	
	private LinkedList<String> schedule = new LinkedList<String>();
	
	private HashMap<String, Image> images = new HashMap<String, Image>();
	
	private ResourceManager() {
		
	}
	
	private String normalizeIdentifier(String identifier) {
		if(!identifier.startsWith("resources/")) {
			return identifier;
		} else {
			throw new ResourceError("Resource identifiers should not start with resources/");
		}
	}
	
	public void schedule(String identifier) {
		identifier = normalizeIdentifier(identifier);
		if(!this.schedule.contains(identifier) && !this.images.containsKey(identifier)) {
			this.schedule.add(identifier);
		}
	}
	
	private void load(String identifier) {
		try {
			if(identifier.endsWith(".png")) {
				this.images.put(identifier, new Image(identifier));
			} else {
				throw new ResourceError("Unkown ressource type: " + identifier);
			}
			// this.schedule.remove(identifier);
		} catch(SlickException e) {
			throw new ResourceError(e);
		}
	}
	
	public Image getImage(String id) {
		id = normalizeIdentifier(id);
		Image image = this.images.get(id);
		if(image == null) {
			load(id);
			return this.images.get(id);
		} else {
			return image;
		}
	}
	
	public Image getRevertedImage(String id) {
		id = normalizeIdentifier(id);
		Image image = this.images.get(id + "?reverse");
		if(image == null) {
			Image reverted = getImage(id).getFlippedCopy(true, false);
			this.images.put(id + "?reverse", reverted);
			return reverted;
		} else {
			return image;
		}
	}
	
	public void load() {
		for(String id: this.schedule) {
			load(id);
		}
	}
	
	public static ResourceManager getInstance() {
		if(instance == null) {
			instance = new ResourceManager();
		}
		return instance;
	}
}
