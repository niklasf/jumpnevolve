package com.googlecode.jumpnevolve.math;

/**
 * Ein einfache Punktlinie zwischen zwei Punkten.
 * 
 * Die Punkte sind Bestandteil der Linie.
 * 
 * @author Erik Wagner
 * 
 */
public class PointLine {

	public final Vector p1, p2;
	public final float m, b;

	public PointLine(Vector p1, Vector p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.m = (p2.y - p1.y) / (p2.x - p1.x);
		this.b = p1.y - m * p1.x;
	}

	/**
	 * Kontrolliert, ob diese Punktlinie mit einer zweiten Punktlinie schneidt
	 * 
	 * @param other
	 *            Die andere Punktlinie
	 * @return <code>true</code>, wenn sich die Linie schneiden
	 */
	public boolean crossesLine(PointLine other) {
		float x = (other.b - this.b) / (this.m - other.m);
		float min = p1.x, max = p1.x;
		min = Math.min(this.p2.x, min);
		max = Math.max(this.p2.x, max);
		min = Math.min(other.p1.x, min);
		max = Math.max(other.p1.x, max);
		min = Math.min(other.p2.x, min);
		max = Math.max(other.p2.x, max);
		return x >= min && x <= max;
	}

	/**
	 * @return Die linkeste Position dieser Linie
	 */
	public float getLeftEnd() {
		return Math.min(this.p1.x, this.p2.x);
	}

	/**
	 * @return Die rechteste Position dieser Linie
	 */
	public float getRightEnd() {
		return Math.max(this.p1.x, this.p2.x);
	}

	/**
	 * @return Die höchste Position dieser Linie
	 */
	public float getUpperEnd() {
		return Math.min(this.p1.y, this.p2.y);
	}

	/**
	 * @return Die unterste Position dieser Linie
	 */
	public float getLowerEnd() {
		return Math.max(this.p1.y, this.p2.y);
	}
}
