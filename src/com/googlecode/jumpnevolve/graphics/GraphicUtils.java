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

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Zeichnet Obhekte in einen OpenGL Grafikkontext.
 * 
 * @author Niklas Fiekas
 */
public class GraphicUtils {

	/**
	 * Zeichnet einen Kreis.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param circle
	 *            Kreis
	 */
	public static void draw(Graphics g, Circle circle) {
		Rectangle rect = circle.getBoundingRectangle();
		g.drawOval(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Zeichnet ein Rechteck.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param rect
	 *            Rechteck
	 */
	public static void draw(Graphics g, Rectangle rect) {
		g.drawRect(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Zeichnet eine Figur.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param shape
	 *            Figur
	 */
	public static void draw(Graphics g, Shape shape) {
		if (shape instanceof Circle) {
			draw(g, (Circle) shape);
		} else if (shape instanceof Rectangle) {
			draw(g, (Rectangle) shape);
		}
	}

	/**
	 * Zeichnet einen Vektor ausgehend vom Ursprung.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param vector
	 *            Vektor
	 */
	public static void draw(Graphics g, Vector vector) {
		g.drawLine(0, 0, vector.x, vector.y);
	}

	/**
	 * Zeichnet einen Vektor ausgehend von einem Orsvektor.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param position
	 *            Ortsvektor
	 * @param vector
	 *            Vektor
	 */
	public static void draw(Graphics g, Vector position, Vector vector) {
		g.drawLine(position.x, position.y, position.x + vector.x, position.y
				+ vector.y);
	}
}
