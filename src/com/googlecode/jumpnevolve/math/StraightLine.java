package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 *
 */
public class StraightLine extends Line {

	/**
	 * Erzeugt eine neue unbegrenzte Gerade aus zwei Punkten, die auf der Gerade
	 * liegen
	 *
	 * @param p1
	 *            Der Ortsvektor des einen Punktes
	 * @param p2
	 *            Der Ortsvektor des anderen Punktes
	 */
	public StraightLine(Vector p1, Vector p2) {
		super(p1, p2);
	}

	@Override
	public boolean canPointBeOnLine(Vector point) {
		return true;
	}

}
