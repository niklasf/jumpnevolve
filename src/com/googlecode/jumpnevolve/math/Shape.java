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
	 * Prüft, ob sich die geometrische Figuren überlappen. Eine Berührung ist
	 * keine Kollision.
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

	/**
	 * @return Die x-Koordinate des linken Endes des Objekts
	 */
	public float getLeftEnd();

	/**
	 * @return Die x-Koordinate des rechten Endes des Objekts
	 */
	public float getRightEnd();

	/**
	 * 
	 * @param other
	 *            Das andere geometricsche Objekt, das dieses Objekt trifft
	 * @param velocity
	 *            Die Geschwindigkeit mit der sich die Objekte auf einander
	 *            zubewegt haben
	 * @return Die Seite dieses Objekts, die berührt wird, in Form eines Bytes
	 *         (vgl. Konstanten)
	 */
	public byte getTouchedSideOfThis(Shape other, Vector velocity);

	/**
	 * 
	 * @param p
	 *            Der Punkt in Form eines Ortvektors
	 * @return Befindet sich der Punkt in der Figur oder nicht
	 */
	public boolean isPointInThis(Vector p);

	/*
	 * Konstanten, die die Seiten bezeichnen
	 */
	public final static byte OBEN = 0;
	public final static byte UNTEN = 1;
	public final static byte RECHTS = 2;
	public final static byte LINKS = 3;

	/*
	 * Konstanten, die die Ecken bezeichnen
	 */
	public final static byte OBEN_RECHTS = 4;
	public final static byte UNTEN_RECHTS = 5;
	public final static byte OBEN_LINKS = 6;
	public final static byte UNTEN_LINKS = 7;

	/*
	 * Konstante, die bei einem Fehler ausgegeben wird (wenn die Berechnung
	 * nicht greift)
	 */
	public final static byte KEIN_ERGEBNIS = -1;
}
