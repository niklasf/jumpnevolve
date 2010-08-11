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
 * Klasse für Vektoren mit einfacher Gleitkommagenauigkeit.
 * 
 * @author Erik Wagner
 */
public class Vector implements Cloneable {

	/**
	 * Der Vector (0, 1) zeigt nach unten
	 */
	public static final Vector DOWN = new Vector(0, 1);

	/**
	 * Der Vektor (-1, 0) zeigt nach links
	 */
	public static final Vector LEFT = new Vector(-1, 0);

	/**
	 * Der Vektor (1, 0) zeigt nach rechts
	 */
	public static final Vector RIGHT = new Vector(1, 0);

	/**
	 * Der Vector (0, -1) zeigt nach oben
	 */
	public static final Vector UP = new Vector(0, -1);

	/**
	 * Der Vektor (0, 0) hat keine Richtung
	 */
	public static final Vector ZERO = new Vector(0, 0);

	/**
	 * Die X-Koordinate
	 */
	public final float x;

	/**
	 * Die Y-Koordinate
	 */
	public final float y;

	/**
	 * Erzeugt einen neuen Vektor.
	 * 
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 */
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return Der Betrag bzw. die Länge des Vektors
	 */
	public float abs() {
		return (float) (Math.sqrt(this.x * this.x + this.y * this.y));
	}

	/**
	 * Addiert zum Vektor einen anderen.
	 * 
	 * @param vec
	 *            Summand
	 * @return Summe
	 */
	public Vector add(Vector vec) {
		return new Vector(this.x + vec.x, this.y + vec.y);
	}

	@Override
	public Object clone() {
		return this;
	}

	/**
	 * Dividiert den Vektor.
	 * 
	 * @param scalar
	 *            Divisor
	 * @return Quotient
	 */
	public Vector div(float scalar) {
		return new Vector(this.x / scalar, this.y / scalar);
	}

	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof Vector) {
			Vector vec = (Vector) object;
			return vec.x == this.x && vec.y == this.y;
		} else {
			return false;
		}
	}

	/**
	 * @return Ein Einheitsvektor, der die Richtung beschreibt.
	 */
	public Vector getDirection() {
		float abs = this.abs();
		return new Vector(this.x / abs, this.y / abs);
	}

	/**
	 * @return Der Abstand zwischen den beiden Orten, die durch die Vektoren
	 *         beschrieben werden.
	 */
	public float getDistance(Vector other) {
		float deltaX = this.x - other.x;
		float deltaY = this.y - other.y;
		return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

	/**
	 * @return Ein Vektor, der zu diesem normal ist und die selbe Länge hat.
	 */
	public Vector getNormal() {
		return new Vector(-this.y, this.x);
	}

	@Override
	public int hashCode() {
		return (int) this.squareAbs();
	}

	/**
	 * Prüft, ob der Betrag des Vektors 0 ist. Für Vektoren mit dem Betrag 0 ist
	 * keine Richtung definiert.
	 * 
	 * @return {@code true}, wenn der Betrag 0 ist.
	 */
	public boolean isZero() {
		return this.x == 0 && this.y == 0;
	}

	/**
	 * Erzeugt einen neuen Vektor.
	 * 
	 * @param x
	 *            Nur der X-Wert wird verändert
	 * @return Der neue Vektor
	 */
	public Vector modifyX(float x) {
		return new Vector(x, this.y);
	}

	/**
	 * Erzeugt einen neuen Vektor.
	 * 
	 * @param y
	 *            Nur der Y-Wert wird verändert
	 * @return Der neue Vektor
	 */
	public Vector modifyY(float y) {
		return new Vector(this.x, y);
	}

	/**
	 * Multipliziert den Vektor.
	 * 
	 * @param scalar
	 *            Faktor
	 * @return Produkt
	 */
	public Vector mul(float scalar) {
		return new Vector(this.x * scalar, this.y * scalar);
	}

	/**
	 * Berechnet das Skalarprodukt mit einem Vektor.
	 * 
	 * @param vec
	 *            Zweiter Vektor
	 * @return Skalarprodukt
	 */
	public float mul(Vector vec) {
		return this.x * vec.x + this.y * vec.y;
	}

	/**
	 * Multipliziert zwei Vektoren wie Listen:
	 * 
	 * <pre>
	 * (a, b) # (c, d) => (a*c, b*d)
	 * </pre>
	 * 
	 * @param vector
	 *            Zweiter Vektor
	 * @return Listenprodukt
	 */
	public Vector mulAsLists(Vector vector) {
		return new Vector(this.x * vector.x, this.y * vector.y);
	}

	/**
	 * Ein monotoner Hash-Wert für die Länge des Vektors, der effizienter
	 * berechnet wird, als die Länge selbst. Eignet sich gut, um Längen zu
	 * vergleichen.
	 * 
	 * @return Das Quadrat der Länge des Vektors
	 */
	public float squareAbs() {
		return this.x * this.x + this.y * this.y;
	}

	/**
	 * Subtrahiert vom Vektor einen anderen.
	 * 
	 * @param vec
	 *            Subtrahend
	 * @return Differenz
	 */
	public Vector sub(Vector vec) {
		return new Vector(this.x - vec.x, this.y - vec.y);
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
