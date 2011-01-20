package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 * 
 */
public class CollisionResult {

	private boolean intersecting = true;
	private Vector minimumOverlap = Vector.ZERO;

	public CollisionResult() {
	}

	private CollisionResult(boolean intersecting, Vector minimumOverlap) {
		this.intersecting = intersecting;
		this.minimumOverlap = minimumOverlap;
	}

	/**
	 * Setzt den Status der Kollision auf <code>false</code>
	 */
	public void setNotIntersecting() {
		this.intersecting = false;
	}

	/**
	 * @return Der Status der Kollision
	 */
	public boolean isIntersecting() {
		return intersecting;
	}

	/**
	 * Setzt die minimale Strecke zum Verschieben des Objekts
	 * 
	 * @param minimumTranslation
	 *            Die Verschiebungsstrecke
	 */
	public void setMinimumOverlap(Vector minimumTranslation) {
		this.minimumOverlap = minimumTranslation;
	}

	/**
	 * @return Die Verschiebungsstrecke
	 */
	public Vector getMinimumOverlap() {
		return minimumOverlap;
	}

	/**
	 * @return Ein CollisionResult, bei dem der Overlap in die entgegengesetzte
	 *         Richtung zeigt
	 */
	public CollisionResult invert() {
		return new CollisionResult(this.intersecting, this.minimumOverlap.neg());
	}
}
