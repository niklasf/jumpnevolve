package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 *
 */
public class Ray extends Line {

	public final Vector p1, p2;

	/**
	 * Erzeugt einen neuen Strahl /Halbgerade
	 *
	 * @param p1
	 *            Der Ausgangspunkt des Strahls (ein Ortsvektor)
	 * @param p2
	 *            Die Richtung des Strahls (ein Richtungsvektor)
	 */
	public Ray(Vector p1, Vector p2) {
		super(p1, p1.add(p2));
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public boolean canPointBeOnLine(Vector point) {
		if (p2.x < 0) {
			if (p2.y < 0) {
				return point.x <= p1.x && point.y <= p1.y;
			} else {
				return point.x <= p1.x && point.y >= p1.y;
			}
		} else {
			if (p2.y < 0) {
				return point.x >= p1.x && point.y <= p1.y;
			} else {
				return point.x >= p1.x && point.y >= p1.y;
			}
		}
	}
}
