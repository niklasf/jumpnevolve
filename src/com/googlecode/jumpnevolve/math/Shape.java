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

/**
 * Allgemeine Klasse für eine geometrische Figur.
 * 
 * @author Niklas Fiekas
 */
public interface Shape extends Serializable {
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
	 * @return Die X-Koordinate des linken Endes des Objekts
	 */
	public float getLeftEnd();

	/**
	 * @return Die X-Koordinate des rechten Endes des Objekts
	 */
	public float getRightEnd();

	/**
	 * @return Die Y-Koordinate des oberen Endes des Objekts
	 */
	public float getUpperEnd();

	/**
	 * @return Die Y-Koordinate des unteren Endes des Objekts
	 */
	public float getLowerEnd();

	/**
	 * @param direction
	 *            Die Seite
	 * @return Die Entfernung vom Zentrum des Objekts zu der entsprechenden
	 *         Seite
	 */
	public float getDistanceToSide(byte direction);

	/**
	 * 
	 * @param other
	 *            Das andere geometricsche Objekt, das dieses Objekt trifft
	 * @return Die Seite dieses Objekts, die berührt wird als {@link Vector#UP},
	 *         {@link Vector#DOWN}, {@link Vector#LEFT} oder
	 *         {@link Vector#RIGHT}.
	 * @Deprecated Es sollte getCollision verwendet werden
	 */
	@Deprecated
	public byte getTouchedSideOfThis(Shape other);

	/**
	 * @param other
	 *            Das andere Shape
	 * @return Ein Kollisionsobjekt, das die Kollision von diesem Objekt zum
	 *         anderen darstellt
	 * @Deprecated Es sollte getCollision mit moveable-booleans verwendet werden
	 *             verwendet werden
	 */
	@Deprecated
	public Collision getCollision(Shape other);

	/**
	 * @param other
	 *            Das andere Shape
	 * @param moveable
	 *            Ob das andere Objekt beweglich ist
	 * @return Ein Kollisionsobjekt, das die Kollision von diesem Objekt zum
	 *         anderen darstellt
	 */
	public Collision getCollision(Shape other, boolean otherMoveable,
			boolean thisMoveable);

	/**
	 * 
	 * @param p
	 *            Der Punkt in Form eines Ortvektors.
	 * @return {@code true}, wenn der Punkt sich in der Figur befindet.
	 */
	public boolean isPointInThis(Vector p);

	/**
	 * @param center
	 *            Der neue Ortsvektor des Mittelpunktes.
	 * @return Eine neue Figur, die sich nur durch den Mittelpunkt von dieser
	 *         unterscheidet. Die Figur selbst wird nicht verändert.
	 */
	public Shape modifyCenter(Vector center);

	/**
	 * @return Eine Slick Version dieser Form.
	 */
	public org.newdawn.slick.geom.Shape toSlickShape();

	/**
	 * @return Die Dimensionen des Shapes
	 */
	public Vector getDimensions();

	/**
	 * @return Die Distanz, die das Objekt in x-Richtung einschließt (vom linken
	 *         Ende bis zum rechten Ende)
	 */
	public float getXRange();

	/**
	 * @return Die Distanz, die das Objekt in y-Richtung einschließt (vom oberen
	 *         Ende bis zum unteren Ende)
	 */
	public float getYRange();

	/*
	 * Konstanten, die die Seiten bezeichnen TODO: Enums oder Vektoren verwenden
	 * 
	 * So beschrieben, dass der negierte Wert die gegenüberliegende Seite
	 * bezeichnet
	 */
	/**
	 * Konstante, die die obere Seite des Objekts bezeichnet
	 */
	public final static byte UP = 1;
	/**
	 * Konstante, die die rechte Seite des Objekts bezeichnet
	 */
	public final static byte RIGHT = 2;
	/**
	 * Konstante, die die links Seite des Objekts bezeichnet
	 */
	public final static byte LEFT = -RIGHT;
	/**
	 * Konstante, die die untere Seite des Objekts bezeichnet
	 */
	public final static byte DOWN = -UP;

	/*
	 * Konstanten, die die Ecken bezeichnen TODO: Enums oder Vektoren verwenden
	 * 
	 * So beschrieben, dass der negierte Wert die gegenüberliegende Ecke
	 * bezeichnet
	 */
	/**
	 * Konstante, die die obere, rechte Ecke des Objekts bezeichnet
	 */
	public final static byte UP_RIGHT = 3;
	/**
	 * Konstante, die die untere, rechte Ecke des Objekts bezeichnet
	 */
	public final static byte DOWN_RIGHT = 4;
	/**
	 * Konstante, die die obere, linke Ecke des Objekts bezeichnet
	 */
	public final static byte UP_LEFT = -DOWN_RIGHT;
	/**
	 * Konstante, die die untere, linke Ecke des Objekts bezeichnet
	 */
	public final static byte DOWN_LEFT = -UP_RIGHT;

	/*
	 * Konstante, die bei einem Fehler ausgegeben wird (wenn die Berechnung
	 * nicht greift) TODO: Enums, Vektoren oder Exceptions verwenden
	 */
	public final static byte NULL = 0;
}
