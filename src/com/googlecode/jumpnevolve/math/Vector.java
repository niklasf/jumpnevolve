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

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasse für Vektoren mit einfacher Gleitkommagenauigkeit.
 * 
 * @author Erik Wagner
 */
public class Vector implements Cloneable, Serializable {

	private static final long serialVersionUID = -8523089593491945245L;

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
		return add(vec.x, vec.y);
	}

	/**
	 * @see #add(Vector)
	 * 
	 * @param vx
	 *            Das erste Element des Summanden
	 * @param vy
	 *            Das zweite Element des Summanden
	 * @return Summe
	 */
	public Vector add(float vx, float vy) {
		return new Vector(this.x + vx, this.y + vy);
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
			return equals(vec.x, vec.y);
		} else {
			return false;
		}
	}

	/**
	 * @param vx
	 *            Das erste Element des zweiten Vektors
	 * @param vy
	 *            Das zweite Element des zweiten Vektors
	 * @return {@code true}, wenn beide Vektoren identisch sind.
	 */
	public boolean equals(float vx, float vy) {
		return this.x == vx && this.y == vy;
	}

	/**
	 * @return Ein Einheitsvektor, der die Richtung beschreibt.
	 */
	public Vector getDirection() {
		float abs = this.abs();
		return new Vector(this.x / abs, this.y / abs);
	}

	/**
	 * @param other
	 *            Der zweite Ortsvektor
	 * @return Der Abstand zwischen den beiden Orten, die durch die Vektoren
	 *         beschrieben werden.
	 */
	public float getDistance(Vector other) {
		return getDistance(other.x, other.y);
	}

	/**
	 * @see #getDistance(Vector)
	 * 
	 * @param ox
	 *            Das erste Element des zweiten Ortsvektors
	 * @param oy
	 *            Das zweite Element des zweiten Ortsvektors
	 * @return Der Abstand zwischen den beiden Orten, die durch die Vektoren
	 *         beschrieben werden.
	 */
	public float getDistance(float ox, float oy) {
		float deltaX = this.x - ox;
		float deltaY = this.y - oy;
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
	 * @see #mul(Vector)
	 * 
	 * @param vx
	 *            Das erste Element des zweiten Vektors
	 * @param vy
	 *            Das zweite Element des zweiten Vektors
	 * @return Skalarprodukt
	 */
	public float mul(float vx, float vy) {
		return this.x * vx + this.y * vy;
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
		return mulAsLists(vector.x, vector.y);
	}

	/**
	 * @see #mulAsLists(Vector)
	 * 
	 * @param vx
	 *            Das erste Element des zweiten Vektors
	 * @param vy
	 *            Das zweite Element des zweiten Vektors
	 * @return Listenprodukt
	 */
	public Vector mulAsLists(float vx, float vy) {
		return new Vector(this.x * vx, this.y * vy);
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
		return sub(vec.x, vec.y);
	}

	/**
	 * @see #sub(Vector)
	 * 
	 * @param vx
	 *            Das erste Element des Subtrahenten
	 * @param vy
	 *            Das zweite Element des Subtrahenten
	 * @return Differenz
	 */
	public Vector sub(float vx, float vy) {
		return new Vector(this.x - vx, this.y - vy);
	}

	public Vector neg() {
		return Vector.ZERO.sub(this);
	}

	/**
	 * Gibt an, ob der Vektor <b>stärker</b> nach oben ({@link #UP}) zeigt, als
	 * der andere. Dabei wird nur die Richtung, nicht der Betrag beachtet.
	 * 
	 * @param other
	 *            Der andere Vektor
	 * @return true, wenn dieser Vektor mehr nach oben zeigt als der andere,
	 *         sonst false
	 */
	public boolean isMoreUpwards(Vector other) {
		return getDirection().y < other.getDirection().y;
	}

	/**
	 * @param other
	 *            Ein weiterer Vektor
	 * @return Der Innenwinkel zwischen den beiden Vektoren im Bogenmaß.
	 */
	public float ang(Vector other) {
		return (float) Math.acos(mul(other) / abs() / other.abs());
	}

	/**
	 * Der Innenwinkel im Bogenmaß zwischen dem Vektor und einem Vektor, der
	 * nach oben zeigt.
	 * 
	 * @see #UP
	 * @see #ang(Vector)
	 */
	public float ang() {
		return ang(Vector.UP);
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
	
	private static Pattern vectorPattern = Pattern.compile("\\s*\\(?\\s*([0-9.eE+-]+)\\s*(\\||,)\\s*([0-9.eE+-]+)\\s*\\)?\\s*");
	
	/**
	 * @param s Die Stringdarstellung eines Vektors.
	 * @return Einer neuer, passender Vektor.
	 * @throws NumberFormatException Wenn der String kein Vektor ist.
	 */
	public static Vector parseVector(String s) {
		Matcher matcher = vectorPattern.matcher(s);
		if(matcher.matches()) {
			return new Vector(Float.parseFloat(matcher.group(1)), Float.parseFloat(matcher.group(3)));
		} else {
			throw new NumberFormatException("Input is no vector: " + s);
		}
	}
}
