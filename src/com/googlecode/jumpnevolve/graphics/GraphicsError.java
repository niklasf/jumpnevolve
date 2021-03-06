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
 * Fehler, der geworfen wird, wenn es Grafikprobleme gibt, die wahrscheinlich
 * nicht auf Java Ebene gelöst werden können.
 * 
 * @author Niklas Fiekas
 */
public class GraphicsError extends Error {

	private static final long serialVersionUID = -5309858224099417866L;

	public GraphicsError() {
		super();
	}

	public GraphicsError(Throwable e) {
		super(e);
	}

	public GraphicsError(String description) {
		super(description);
	}
}
