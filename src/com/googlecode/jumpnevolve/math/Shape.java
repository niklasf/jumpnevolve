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

package com.googlecode.jumpnevolve.math;

/**
 * Allgemeine Klasse für eine geometrische Figur.
 * 
 * @author Niklas Fiekas
 */
public interface Shape {
	/**
	 * <p>
	 * Prüft, ob sich die geometrische Figuren überlappen. Eine Berührung
	 * ist keine Kollision.
	 * </p>
	 * 
	 * <p>
	 * Anforderungen an die Funktion:
	 * </p>
	 * <ul>
	 * <li><strong>Symmetrie:</strong> {@code a.doesCollide(b) ==
	 * b.doesCollide(a)}</li>
	 * <li>Sollte möglichst viele andere Figuren kennen. Falls nicht, steht noch
	 * {@link #getBestCircle()} für eine Näherung zur Verfügung.</li>
	 * </ul>
	 * 
	 * @param other
	 *            Die andere Figur
	 */
	boolean doesCollide(Shape other);

	/**
	 * @return Ein Kreis, der die Figur möglichst gut annähert.
	 */
	public Circle getBestCircle();

	/**
	 * @return Der Ortsvektor des Mittelpunkts.
	 */
	public Vector getCenter();
}
