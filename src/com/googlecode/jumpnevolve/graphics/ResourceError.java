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

/**
 * Wird geworfen, wenn ein Fehler beim Laden von Resourcen auftritt und kein
 * Ersatz verfügbar ist.
 * 
 * @author Niklas Fiekas
 */
public class ResourceError extends Error {

	private static final long serialVersionUID = 3117915741962126340L;

	public ResourceError() {
		super();
	}
	
	public ResourceError(String description) {
		super(description);
	}
	
	public ResourceError(Throwable cause) {
		super(cause);
	}
}
