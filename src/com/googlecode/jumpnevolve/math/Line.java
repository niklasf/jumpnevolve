package com.googlecode.jumpnevolve.math;

/**
 * Ein einfache Punktlinie zwischen zwei Punkten.
 *
 * Die Punkte sind Bestandteil der Linie.
 *
 * @author Erik Wagner
 *
 */
public abstract class Line {

	public final float a, b, c;

	/**
	 * Prüft, ob der Punkt auf der Linie liegen kann (ob er in passender x- bzw.
	 * y-Region liegt)
	 *
	 * @param point
	 *            Der Ortsvektor des Punktes
	 * @return <code>true</code>, wenn der Punkt auf der Linie liegen könnte
	 */
	public abstract boolean canPointBeOnLine(Vector point);

	/**
	 * Erzeugt eine neue Linie, die durch die beiden Punkte verläuft
	 *
	 * @param p1
	 *            Der erste Punkt
	 * @param p2
	 *            Der zweite Punkt
	 */
	public Line(Vector p1, Vector p2) {
		this.a = p1.y - p2.y;
		this.b = p2.x - p1.x;
		this.c = -(this.a * p1.x + this.b * p1.y);
	}

	/**
	 * Prüft, ob sich die beiden Linie schneiden
	 *
	 * @param other
	 *            Die andere Gerade
	 * @return <code>true</code>, wenn sich die Geraden schneiden;
	 *         <code>false</code>, wenn die Geraden parallel oder identisch
	 *         verlaufen
	 */
	public boolean crosses(Line other) {
		boolean canCross = !(this.a * other.b == this.b * other.a
				&& this.b * other.c == this.c * other.b && this.a * other.c == this.c
				* other.a)
				&& !(this.a == 0 && this.b == 0)
				&& !(other.a == 0 && other.b == 0)
				&& !(this.a * other.b == this.b * other.a);
		if (canCross) {
			Vector crossPoint = this.getCrossingPoint(other);
			return this.canPointBeOnLine(crossPoint)
					&& other.canPointBeOnLine(crossPoint);
		} else {
			return false;
		}
	}

	/**
	 * Ermittelt den Schnittpunkt der beiden Geraden
	 *
	 * Es sollte vorher {@link #crosses(Line)} aufgerufen werden
	 *
	 * @param other
	 *            Die andere Gerade
	 * @return Der Schnittpunkt der Geraden
	 */
	public Vector getCrossingPoint(Line other) {
		if (!(this.a * other.b == other.a * this.b)) {
			float q = this.a * other.b - other.a * this.b;
			float x = (this.b * other.c - this.c * other.b) / q;
			float y = (this.c * other.a - this.a * other.c) / q;
			return new Vector(x, y);
		}

		if (this.a != 0) {
			float y = (other.a / this.a * this.c - other.c)
					/ (other.b - other.a / this.a * this.b);
			float x = (this.b * y + this.c) / (-this.a);
			return new Vector(x, y);
		} else {
			float x = (other.b / this.b * this.c - other.c)
					/ (other.a - other.b / this.b * this.a);
			float y = (this.a * x + this.c) / (-this.b);
			return new Vector(x, y);
		}
	}

	/**
	 * Prüft, ob der Punkt auf der Linie liegt
	 *
	 * @param point
	 *            Der Ortsvektor des Punktes
	 * @return <code>true</code>, wenn der Punkt auf der Linie liegt
	 */
	public boolean isPointOnLine(Vector point) {
		return this.canPointBeOnLine(point)
				&& this.a * point.x + this.b * point.y + this.c == 0;
	}

	/**
	 * @return Der Normalvektor zu dieser Geraden
	 */
	public Vector getNormalVector() {
		return new Vector(a, b);
	}

	/**
	 * @param point
	 *            Der Ortsvektor eines beliebigen Punktes
	 * @return Die Distanz zwischen dem Punkt und dieser Geraden
	 */
	public float getDistanceTo(Vector point) {
		return (float) ((this.a * point.x + this.b * point.y + this.c) / Math
				.sqrt(this.a * this.a + this.b * this.b));
	}

	/**
	 * @param point
	 *            Der Ortsvektor eines beliebigen Punktes
	 * @return Der Vektor zwischen dem Punkt und dieser Geraden
	 */
	public Vector getDistanceVectorTo(Vector point) {
		return this.getNormalVector().mul(this.getDistanceTo(point));
	}

	public float getX(float y) {
		if (a != 0) {
			return -(this.b * y + this.c) / this.a;
		} else {
			return 0.0f;
		}
	}

	public float getY(float x) {
		if (this.b != 0) {
			return -(this.a * x + this.c) / this.b;
		} else {
			return 0.0f;
		}
	}

	public final boolean arePointsOnTheSameSide(Vector point1, Vector point2) {
		return Math.signum(this.getDistanceTo(point1)) == Math.signum(this
				.getDistanceTo(point2));
	}

	@Override
	public String toString() {
		return "Line: " + this.a + "*x + " + this.b + "*y + " + this.c;
	}
}
