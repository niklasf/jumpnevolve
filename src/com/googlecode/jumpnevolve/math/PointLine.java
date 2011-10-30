package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 *
 */
public class PointLine extends Line {

	public final Vector p1, p2;

	/**
	 * Erzeugt eine neue Linie zwischen zwei Punkten, welche mit Bestandteil der
	 * Linie selbst sind
	 *
	 * @param p1
	 *            Der eine Punkt
	 * @param p2
	 *            Der andere Punkt
	 */
	public PointLine(Vector p1, Vector p2) {
		super(p1, p2);
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public boolean canPointBeOnLine(Vector point) {
		if (p1.equals(p2)) {
			return false;
		} else {
			Vector halfDist = p2.sub(p1).div(2);
			return new Circle(p1.add(halfDist), halfDist).isPointInThis(point);
		}
	}

	@Override
	public String toString() {
		return "PointLine: " + this.a + "*x + " + this.b + "*y + " + this.c
				+ " Punkt1: " + this.p1 + " Punkt2: " + this.p2;
	}
}
