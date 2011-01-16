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
 * Diese Klasse speichert Rechtecke und ermöglicht Berechnungen damit.
 */
public class Rectangle implements Shape {

	private static final long serialVersionUID = -4556824470497571683L;

	/**
	 * Die X-Koordinate der oberen linken Ecke
	 */
	public final float x;

	/**
	 * Die Y-Koordinate der oberen linken Ecke
	 */
	public final float y;

	/**
	 * Die Breite
	 */
	public final float width;

	/**
	 * Die Höhe
	 */
	public final float height;

	private void testMetrics(float width, float height) {
		if (width == 0 || height == 0) {
			throw new IllegalArgumentException(
					"All rectangles must have width and height.");
		}
	}

	/**
	 * <p>
	 * Erzeugt ein neues Rechteck.
	 * </p>
	 * 
	 * <p>
	 * Wird eine negative Breite oder Höhe übergeben, werden die Parameter so
	 * neu bestimmt, dass x|y die obere linke Ecke bleibt und Breite und Höhe
	 * positiv sind.
	 * </p>
	 * 
	 * @param x
	 *            Die X-Koordinate der oberen linken Ecke
	 * @param y
	 *            Die Y-Koordinate der oberen linken Ecke
	 * @param width
	 *            Die Breite
	 * @param height
	 *            Die Höhe
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn Breite oder Höhe {@code 0} sind.
	 */
	public Rectangle(float x, float y, float width, float height) {
		testMetrics(width, height);
		if (width < 0) {
			x += width;
			width = -width;
		}
		if (height < 0) {
			y += height;
			height = -height;
		}
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * <p>
	 * Erzeugt ein neues Rechteck.
	 * </p>
	 * 
	 * <p>
	 * Zwischen positiven und negativen Breiten- und Höhenangaben wird nicht
	 * unterschieden.
	 * </p>
	 * 
	 * @param center
	 *            Ortsvektor des Mittelpunktes
	 * @param width
	 *            Breite
	 * @param height
	 *            Höhe
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn Breite oder Höhe {@code 0} sind.
	 */
	public Rectangle(Vector center, float width, float height) {
		testMetrics(width, height);
		width = Math.abs(width);
		height = Math.abs(height);
		this.x = center.x - width / 2.0f;
		this.y = center.y - height / 2.0f;
		this.width = width;
		this.height = height;
	}

	/**
	 * @see #Rectangle(Vector, float, float)
	 * 
	 * @param center
	 *            Ortsvektor des Mittelpunkts
	 * @param centerToCorner
	 *            Vektor, der vom Zentrum auf eine Ecke des Rechtecks zeigt
	 */
	public Rectangle(Vector center, Vector centerToCorner) {
		this(center, centerToCorner.x * 2, centerToCorner.y * 2);
	}

	public Rectangle(org.newdawn.slick.geom.Rectangle rect) {
		this(rect.getCenterX(), rect.getCenterY(), rect.getWidth(), rect
				.getHeight());
	}

	/**
	 * @return Die Fläche des Rechtecks
	 */
	public float getArea() {
		return this.width * this.height;
	}

	public Vector getCenter() {
		return new Vector(this.x + this.width / 2.0f, this.y + this.height
				/ 2.0f);
	}

	public Circle getBestCircle() {
		return new Circle(getCenter(), (this.width + this.height) / 4.0f);
	}

	/**
	 * @return Der Umkreis des Rechtecks. Alle vier Ecken liegen auf dem
	 *         Kreisrand.
	 */
	public Circle getBoundingCircle() {
		return new Circle(getCenter(), 0.5f * (float) Math.sqrt(this.width
				* this.width + this.height * this.height));
	}

	/**
	 * @return Der Ortsvektor der unteren rechten Ecke
	 */
	public Vector getLowerRightCorner() {
		return new Vector(this.x + this.width, this.y + this.height);
	}

	/**
	 * @param other
	 *            Ein zweites Rechteck
	 * @return Das kleinste Rechteck das beide Rechtecke vollständig enthält.
	 */
	public Rectangle getBoundingRectangle(Rectangle other) {
		// Obere linke Ecke
		float left = Math.min(this.x, other.x);
		float top = Math.min(this.y, other.y);

		// Untere Rechte Ecke
		Vector thisCorner = getLowerRightCorner();
		Vector thatCorner = other.getLowerRightCorner();
		float right = Math.max(thisCorner.x, thatCorner.x);
		float bottom = Math.max(thisCorner.y, thatCorner.y);

		// Rechteck erzeugen
		return new Rectangle(left, top, right - left, bottom - top);
	}

	public boolean doesCollide(Shape shape) {
		if (shape instanceof Rectangle) {
			// http://www.back-side.net/codingrects.html
			Rectangle other = (Rectangle) shape;
			Rectangle bounding = other.getBoundingRectangle(this);
			return bounding.width <= this.width + other.width
					&& bounding.height <= this.height + other.height;
		} else {
			return shape.getBestCircle().doesCollide(this);
		}
	}

	@Override
	public int hashCode() {
		return (int) (this.getArea() + this.getCenter().hashCode());
	}

	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) object;
			return rectangle.x == this.x && rectangle.y == this.y
					&& rectangle.width == this.width
					&& rectangle.height == this.height;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + "): " + this.width + " x "
				+ this.height;
	}

	@Override
	public float getLeftEnd() {
		return this.x;
	}

	@Override
	public float getRightEnd() {
		return this.x + this.width;
	}

	public Vector getHighLeftCorner() {
		return this.getCenter().add(
				new Vector(-this.width / 2.0f, -this.height / 2.0f));
	}

	public Vector getHighRightCorner() {
		return this.getCenter().add(
				new Vector(this.width / 2.0f, -this.height / 2.0f));
	}

	public Vector getLowLeftCorner() {
		return this.getCenter().add(
				new Vector(-this.width / 2.0f, this.height / 2.0f));

	}

	public Vector getLowRightCorner() {
		return this.getCenter().add(
				new Vector(this.width / 2.0f, this.height / 2.0f));

	}

	@Override
	public boolean isPointInThis(Vector p) {
		if (p.x < this.x || p.y < this.y || p.x > this.x + this.width
				|| p.y > this.y + this.height) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public float getUpperEnd() {
		return this.getHighLeftCorner().y;
	}

	@Override
	public float getLowerEnd() {
		return this.getLowLeftCorner().y;
	}

	@Override
	public Shape modifyCenter(Vector center) {
		return new Rectangle(center, this.width, this.height);
	}

	@Override
	public org.newdawn.slick.geom.Shape toSlickShape() {
		return new org.newdawn.slick.geom.Rectangle(this.x, this.y, this.width,
				this.height);
	}

	private ElementalKollision getRectangleCollision(Rectangle other,
			boolean otherMoveable, boolean thisMoveable, boolean firstRound) {
		boolean cornersInside[] = new boolean[4]; // Ecke beginnend oben-links
		// im Uhrzeigersinn
		cornersInside[0] = this.isPointInThis(other.getHighLeftCorner());
		cornersInside[1] = this.isPointInThis(other.getHighRightCorner());
		cornersInside[2] = this.isPointInThis(other.getLowRightCorner());
		cornersInside[3] = this.isPointInThis(other.getLowLeftCorner());
		int numbersOfInsideCorners = 0;
		for (boolean value : cornersInside) {
			if (value) {
				numbersOfInsideCorners++;
			}
		}
		Vector overlap = Vector.ZERO;
		if (numbersOfInsideCorners == 1) {
			if (cornersInside[0]) {
				Vector vec = this.getLowRightCorner().sub(
						other.getHighLeftCorner());
				if (vec.ang(Vector.DOWN_RIGHT) < 0.7) {
					overlap = this.getLowRightCorner().sub(
							other.getHighLeftCorner());
				} else {
					if (vec.x * vec.x > vec.y * vec.y) {
						overlap = new Vector(0.0f, this.getLowerEnd()
								- other.getUpperEnd());
					} else {
						overlap = new Vector(this.getRightEnd()
								- other.getLeftEnd(), 0.0f);
					}
				}
			} else if (cornersInside[1]) {
				Vector vec = this.getLowLeftCorner().sub(
						other.getHighRightCorner());
				if (vec.ang(Vector.DOWN_LEFT) < 0.7) {
					overlap = this.getLowLeftCorner().sub(
							other.getHighRightCorner());
				} else {
					if (vec.x * vec.x > vec.y * vec.y) {
						overlap = new Vector(0.0f, this.getLowerEnd()
								- other.getUpperEnd());
					} else {
						overlap = new Vector(this.getLeftEnd()
								- other.getRightEnd(), 0.0f);
					}
				}
			} else if (cornersInside[2]) {
				Vector vec = this.getHighLeftCorner().sub(
						other.getLowRightCorner());
				if (vec.ang(Vector.UP_LEFT) < 0.7) {
					overlap = this.getHighLeftCorner().sub(
							other.getLowerRightCorner());
				} else {
					if (vec.x * vec.x > vec.y * vec.y) {
						overlap = new Vector(0.0f, this.getUpperEnd()
								- other.getLowerEnd());
					} else {
						overlap = new Vector(this.getLeftEnd()
								- other.getRightEnd(), 0.0f);
					}
				}// Hier weiter machen
			} else if (cornersInside[3]) {
				Vector vec = this.getHighRightCorner().sub(
						other.getLowLeftCorner());
				if (vec.ang(Vector.UP_RIGHT) < 0.7) {
					overlap = this.getHighRightCorner().sub(
							other.getLowLeftCorner());
				} else {
					if (vec.x * vec.x > vec.y * vec.y) {
						overlap = new Vector(0.0f, this.getUpperEnd()
								- other.getLowerEnd());
					} else {
						overlap = new Vector(this.getRightEnd()
								- other.getLeftEnd(), 0.0f);
					}
				}
			}
			return new ElementalKollision(thisMoveable, otherMoveable, overlap);
		} else if (numbersOfInsideCorners == 2) {
			if (cornersInside[0]) {
				if (cornersInside[1]) {
					overlap = new Vector(0.0f, this.getLowerEnd()
							- other.getUpperEnd());
				} else if (cornersInside[3]) {
					overlap = new Vector(this.getRightEnd()
							- other.getLeftEnd(), 0.0f);
				}
			} else if (cornersInside[1]) {
				if (cornersInside[2]) {
					overlap = new Vector(this.getLeftEnd()
							- other.getRightEnd(), 0.0f);
				}
			} else if (cornersInside[2]) {
				if (cornersInside[3]) {
					overlap = new Vector(0.0f, this.getUpperEnd()
							- other.getLowerEnd());
				}
			}
			return new ElementalKollision(thisMoveable, otherMoveable, overlap);
		} else if (numbersOfInsideCorners == 4) {
			return new ElementalKollision(); // Nichts blockieren
			// FIXME: Oder alles blockieren
		} else if (numbersOfInsideCorners == 0 && firstRound == true) {
			return other.getRectangleCollision(this, thisMoveable,
					otherMoveable, false).invertKollision();
		} else if (numbersOfInsideCorners == 0 && firstRound == false) {
			if ((this.getUpperEnd() > other.getUpperEnd() && this.getLowerEnd() < other
					.getLowerEnd())
					|| (this.getRightEnd() < other.getRightEnd() && this
							.getLeftEnd() > other.getLeftEnd())) {
				return new ElementalKollision();// Nichts blockieren
				// FIXME: Oder alles blockieren
			} else {
				return new ElementalKollision(); // Leere Kollision zurückgeben
				// FIXME: Fehler ausgeben
			}
		} else {
			return new ElementalKollision(); // Leere Kollision zurückgeben
			// FIXME: Fehler ausgeben
		}
	}

	@Override
	public ElementalKollision getCollision(Shape other, boolean otherMoveable,
			boolean thisMoveable) {
		if (other instanceof Rectangle) {
			return getRectangleCollision((Rectangle) other, otherMoveable,
					thisMoveable, true);
		} else if (other instanceof Circle) {
			return other.getCollision(this, thisMoveable, otherMoveable)
					.invertKollision();

		} else {
			return other.getBestCircle().getCollision(this, thisMoveable,
					otherMoveable).invertKollision();
		}
	}

	@Override
	public float getDistanceToSide(byte direction) {
		switch (direction) {
		case Shape.UP:
		case Shape.DOWN:
			return this.height / 2.0f;
		case Shape.RIGHT:
		case Shape.LEFT:
			return this.width / 2.0f;
		default:
			return 0;
		}
	}

	@Override
	public float getXRange() {
		return width;
	}

	@Override
	public float getYRange() {
		return height;
	}

	public Vector getCorner(Vector direction) {
		if (direction.x > 0) {
			if (direction.y > 0) {
				return this.getLowRightCorner();
			} else {
				return this.getHighRightCorner();
			}
		} else {
			if (direction.y > 0) {
				return this.getLowLeftCorner();
			} else {
				return this.getHighLeftCorner();
			}
		}
	}
}
