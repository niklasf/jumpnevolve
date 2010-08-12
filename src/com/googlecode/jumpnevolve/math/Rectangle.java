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
	 * @param dimension
	 *            Vektor, der Breite und Höhe beschreibt
	 */
	public Rectangle(Vector center, Vector dimension) {
		this(center, dimension.x, dimension.y);
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
			System.out.println(bounding);
			return bounding.width < this.width + other.width
					&& bounding.height < this.height + other.height;
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

	@Override
	public byte getTouchedSideOfThis(Shape other) {
		// TODO kreise einfügen
		if (other instanceof Rectangle) {
			Vector directionToCorner;
			switch (this.getTouchedCorner((Rectangle) other)) {
			case Shape.OBEN_LINKS:
				directionToCorner = ((Rectangle) other).getLowRightCorner()
						.sub(this.getCenter());
				if (directionToCorner.showMoreUpwards(this.getHighLeftCorner()
						.sub(this.getCenter()))) {
					return Shape.OBEN;
				} else {
					return Shape.LINKS;
				}
			case Shape.OBEN_RECHTS:
				directionToCorner = ((Rectangle) other).getLowLeftCorner().sub(
						this.getCenter());
				if (directionToCorner.showMoreUpwards(this.getHighRightCorner()
						.sub(this.getCenter()))) {
					return Shape.OBEN;
				} else {
					return Shape.RECHTS;
				}
			case Shape.UNTEN_LINKS:
				directionToCorner = ((Rectangle) other).getHighRightCorner()
						.sub(this.getCenter());
				if (directionToCorner.showMoreUpwards(this.getLowLeftCorner()
						.sub(this.getCenter()))) {
					return Shape.LINKS;
				} else {
					return Shape.UNTEN;
				}
			case Shape.UNTEN_RECHTS:
				directionToCorner = ((Rectangle) other).getHighLeftCorner()
						.sub(this.getCenter());
				if (directionToCorner.showMoreUpwards(this.getLowRightCorner()
						.sub(this.getCenter()))) {
					return Shape.RECHTS;
				} else {
					return Shape.UNTEN;
				}
			case Shape.OBEN:
				return Shape.OBEN;
			case Shape.UNTEN:
				return Shape.UNTEN;
			case Shape.RECHTS:
				return Shape.RECHTS;
			case Shape.LINKS:
				return Shape.LINKS;
			default:
				break;
			}
		}
		return Shape.KEIN_ERGEBNIS;
	}

	private byte getTouchedCorner(Rectangle other) {
		if (this.isPointInThis(other.getHighLeftCorner())) {
			if (this.isPointInThis(other.getHighRightCorner())) {
				return Shape.UNTEN;
			} else if (this.isPointInThis(other.getLowLeftCorner())) {
				return Shape.RECHTS;
			}
			return Shape.UNTEN_RECHTS;
		} else if (this.isPointInThis(other.getLowRightCorner())) {
			if (this.isPointInThis(other.getHighRightCorner())) {
				return Shape.LINKS;
			} else if (this.isPointInThis(other.getLowLeftCorner())) {
				return Shape.OBEN;
			}
			return Shape.OBEN_LINKS;
		} else if (this.isPointInThis(other.getLowLeftCorner())) {
			return Shape.OBEN_RECHTS;
		} else if (this.isPointInThis(other.getHighRightCorner())) {
			return Shape.UNTEN_LINKS;
		} else {
			return Shape.KEIN_ERGEBNIS;
		}
	}

	private Vector getHighLeftCorner() {
		return this.getCenter().add(
				new Vector(-this.width / 2.0f, -this.height / 2.0f));
	}

	private Vector getHighRightCorner() {
		return this.getCenter().add(
				new Vector(this.width / 2.0f, -this.height / 2.0f));
	}

	private Vector getLowLeftCorner() {
		return this.getCenter().add(
				new Vector(-this.width / 2.0f, this.height / 2.0f));

	}

	private Vector getLowRightCorner() {
		return this.getCenter().add(
				new Vector(this.width / 2.0f, this.height / 2.0f));

	}

	@Override
	public boolean isPointInThis(Vector p) {
		if (p.x <= this.x || p.y <= this.y || p.x >= this.x + this.width
				|| p.y >= this.y + this.height) {
			return false;
		} else {
			return true;
		}
	}
}
