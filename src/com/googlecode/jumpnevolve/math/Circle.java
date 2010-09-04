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
 * Diese Klasse speichert Kreise und ermöglicht Berechnungen damit.
 */
public class Circle implements Shape {

	private static final long serialVersionUID = -4820100555916995691L;

	/**
	 * Der Ortsvektor des Mittelpunkts
	 */
	public final Vector position;

	/**
	 * Der Radius
	 */
	public final float radius;

	@Override
	public boolean doesCollide(Shape other) {
		if (other instanceof Rectangle) {
			Rectangle rect = (Rectangle) other;
			float testX = this.position.x;
			float testY = this.position.y;
			if (testX < rect.x) {
				testX = rect.x;
			} else if (testX > rect.x + rect.width) {
				testX = rect.x + rect.width;
			}
			if (testY < rect.y) {
				testY = rect.y;
			} else if (testY > rect.y + rect.height) {
				testY = rect.y + rect.height;
			}
			float deltaX = this.position.x - testX;
			float deltaY = this.position.y - testY;
			return deltaX * deltaX + deltaY * deltaY < this.radius
					* this.radius;
		} else {
			return isCircleCircleCollusion(this, other.getBestCircle());
		}
	}

	@Override
	public Circle getBestCircle() {
		return this;
	}

	@Override
	public Vector getCenter() {
		return this.position;
	}

	private static boolean isCircleCircleCollusion(Circle c1, Circle c2) {
		return c1.position.getDistance(c2.position) < c1.radius + c2.radius;
	}

	/**
	 * @return Das kleinste Rechteck, dass den Kreis vollständig enthält.
	 */
	public Rectangle getBoundingRectangle() {
		return new Rectangle(this.position, this.radius * 2.0f,
				this.radius * 2.0f);
	}

	private void testRadius(float radius) {
		if (radius < 0) {
			throw new IllegalArgumentException(
					"Circles can't have a negative radius.");
		}
	}

	/**
	 * Erzeugt einen neuen Kreis.
	 * 
	 * @param x
	 *            X-Koordinate des Mittelpunkts
	 * @param y
	 *            Y-Koordinate des Mittelpunkts
	 * @param radius
	 *            Radius
	 */
	public Circle(float x, float y, float radius) {
		testRadius(radius);
		this.position = new Vector(x, y);
		this.radius = radius;
	}

	/**
	 * Erzeugt einen neuen Kreis.
	 * 
	 * @param position
	 *            Ortsvektor des Mittelpunkts
	 * @param radius
	 *            Radius
	 */
	public Circle(Vector position, float radius) {
		testRadius(radius);
		this.position = position;
		this.radius = radius;
	}

	public Circle(Vector position, Vector radius) {
		float rad = radius.abs();
		testRadius(rad);
		this.position = position;
		this.radius = rad;
	}

	@Override
	public int hashCode() {
		return (int) (this.radius * this.radius + this.position.hashCode());
	}

	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof Circle) {
			Circle circle = (Circle) object;
			return this.position.equals(circle.position)
					&& this.radius == circle.radius;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "M: " + this.position + " r: " + this.radius;
	}

	@Override
	public float getLeftEnd() {
		return this.position.x - this.radius;
	}

	@Override
	public float getRightEnd() {
		return this.position.x + this.radius;
	}

	@Override
	public byte getTouchedSideOfThis(Shape other) {
		if (other instanceof Circle) {
			Vector direction = other.getCenter().sub(this.getCenter());
			float absY = Math.abs(direction.y);
			if (direction.x > absY) {
				return Shape.RECHTS;
			} else if (direction.x < -absY) {
				return Shape.LINKS;
			} else {
				if (direction.y > 0) {
					return Shape.UNTEN;
				} else {
					return Shape.OBEN;
				}
			}
		} else if (other instanceof Rectangle) {
			if (other.getLowerEnd() > this.getCenter().y - this.radius * 0.7f) {
				return Shape.OBEN;
			} else if (other.getUpperEnd() < this.getCenter().y + this.radius
					* 0.7f) {
				return Shape.UNTEN;
			} else if (other.getLeftEnd() > this.getCenter().x + this.radius
					* 0.7f) {
				return Shape.RECHTS;
			} else if (other.getRightEnd() < this.getCenter().x - this.radius
					* 0.7f) {
				return Shape.LINKS;
			} else {
				Rectangle x = new Rectangle(this.getCenter(),
						this.radius * 0.35f, this.radius * 0.35f);
				return x.getTouchedSideOfThis(other);
			}
		}
		return 0;
	}

	@Override
	public boolean isPointInThis(Vector p) {
		return this.getCenter().getDistance(p) < this.radius;
	}

	@Override
	public float getUpperEnd() {
		return this.getCenter().y - this.radius;
	}

	@Override
	public float getLowerEnd() {
		return this.getCenter().y + this.radius;
	}

	@Override
	public Shape modifyCenter(Vector center) {
		return new Circle(center, this.radius);
	}

	@Override
	public org.newdawn.slick.geom.Shape toSlickShape() {
		return new org.newdawn.slick.geom.Circle(this.position.x, this.position.y, this.radius);
	}
}
