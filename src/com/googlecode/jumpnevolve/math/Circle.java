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
			return deltaX * deltaX + deltaY * deltaY <= this.radius
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

	public Circle(org.newdawn.slick.geom.Circle circle) {
		this(circle.getCenterX(), circle.getCenterY(), circle.getRadius());
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
		return new org.newdawn.slick.geom.Circle(this.position.x,
				this.position.y, this.radius);
	}

	@Override
	public ElementalCollision getElementalCollision(Shape other,
			boolean otherMoveable, boolean thisMoveable) {
		if (other instanceof Circle) {
			Vector overlap = other.getCenter().sub(this.getCenter());
			return new ElementalCollision(thisMoveable, otherMoveable, overlap);
		} else if (other instanceof Rectangle) {
			Vector direction;

			if (other.getLowerEnd() >= this.getUpperEnd()
					&& other.getLowerEnd() <= this.getCenter().y) {
				if (other.getRightEnd() >= this.getLeftEnd()
						&& other.getRightEnd() <= this.getCenter().x) {
					direction = Vector.UP_LEFT;
				} else if (other.getLeftEnd() <= this.getRightEnd()
						&& other.getLeftEnd() >= this.getCenter().x) {
					direction = Vector.UP_RIGHT;
				} else {
					direction = Vector.UP;
				}
			} else if (other.getUpperEnd() <= this.getLowerEnd()
					&& other.getUpperEnd() >= this.getCenter().y) {
				if (other.getRightEnd() >= this.getLeftEnd()
						&& other.getRightEnd() < this.getCenter().x) {
					direction = Vector.DOWN_LEFT;
				} else if (other.getLeftEnd() <= this.getCenter().x
						+ this.radius
						&& other.getLeftEnd() >= this.getCenter().x) {
					direction = Vector.DOWN_RIGHT;
				} else {
					direction = Vector.DOWN;
				}
			} else {
				if (other.getRightEnd() >= this.getLeftEnd()
						&& other.getRightEnd() <= this.getCenter().x) {
					direction = Vector.LEFT;
				} else if (other.getLeftEnd() <= this.getCenter().x
						+ this.radius
						&& other.getLeftEnd() >= this.getCenter().x) {
					direction = Vector.RIGHT;
				} else {
					direction = Vector.ZERO;
				}
			}
			Vector referencePoint;
			if (direction.x * direction.y != 0) {
				referencePoint = ((Rectangle) other).getCorner(direction.neg());
			} else {
				if (direction.x == 0) {
					if (direction.y > 0) {
						referencePoint = new Vector(this.position.x, other
								.getUpperEnd());
					} else {
						referencePoint = new Vector(this.position.x, other
								.getLowerEnd());
					}
				} else {
					if (direction.x > 0) {
						referencePoint = new Vector(other.getLeftEnd(),
								this.position.y);
					} else {
						referencePoint = new Vector(other.getRightEnd(),
								this.position.y);
					}
				}
			}
			Vector toReference = referencePoint.sub(this.position);
			Vector overlap = toReference.mul(this.radius / toReference.abs()
					- 1.0f);
			return new ElementalCollision(thisMoveable, otherMoveable, overlap);
		} else {
			return this.getElementalCollision(other.getBestCircle(),
					otherMoveable, thisMoveable);
		}
	}

	@Override
	public float getDistanceToSide(byte direction) {
		return this.radius;
	}

	@Override
	public float getXRange() {
		return radius * 2;
	}

	@Override
	public float getYRange() {
		return radius * 2;
	}

	@Override
	public Collision getCollision(Shape other, boolean otherMoveable,
			boolean thisMoveable) {
		Collision col = new Collision();
		col.addCollision(this.getElementalCollision(other, otherMoveable,
				thisMoveable));
		return col;
	}
}
