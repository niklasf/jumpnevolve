package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 * 
 */
public class CollisionResult {

	private boolean isIntersecting = true;
	private boolean willIntersect = true;
	private Vector isOverlap = Vector.ZERO;
	private Vector willOverlap = Vector.ZERO;
	private final boolean thisMoveable, otherMoveable;

	public CollisionResult(boolean thisMoveable, boolean otherMoveable) {
		this.thisMoveable = thisMoveable;
		this.otherMoveable = otherMoveable;
	}

	private CollisionResult(boolean intersecting, boolean willIntersect,
			Vector isOverlap, Vector willOverlap, boolean thisMoveable,
			boolean otherMoveable) {
		this.isIntersecting = intersecting;
		this.willIntersect = willIntersect;
		this.isOverlap = isOverlap;
		this.willOverlap = willOverlap;
		this.thisMoveable = thisMoveable;
		this.otherMoveable = otherMoveable;
	}

	/**
	 * Setzt den Status der Kollision auf <code>false</code>
	 */
	public void setNotIntersecting() {
		this.isIntersecting = false;
	}

	/**
	 * @return Der Status der Kollision
	 */
	public boolean isIntersecting() {
		return this.isIntersecting;
	}

	/**
	 * Setzt den Status der Kollision auf <code>false</code>
	 */
	public void setWillNotIntersect() {
		this.willIntersect = false;
	}

	/**
	 * @return Der Status der Kollision
	 */
	public boolean willIntersect() {
		return this.willIntersect;
	}

	/**
	 * Setzt die minimale Strecke zum Verschieben des Objekts
	 * 
	 * @param translation
	 *            Die Verschiebungsstrecke
	 */
	public void setIsOverlap(Vector translation) {
		this.isOverlap = translation;
	}

	/**
	 * @return Die Verschiebungsstrecke
	 */
	public Vector getIsOverlap() {
		return this.isOverlap;
	}

	/**
	 * Setzt die minimale Strecke zum Verschieben des Objekts
	 * 
	 * @param translation
	 *            Die Verschiebungsstrecke
	 */
	public void setWillOverlap(Vector translation) {
		this.willOverlap = translation;
	}

	/**
	 * @return Die Verschiebungsstrecke
	 */
	public Vector getWillOverlap() {
		return this.willOverlap;
	}

	/**
	 * @return Ein CollisionResult, bei dem der Overlap in die entgegengesetzte
	 *         Richtung zeigt
	 */
	public CollisionResult invert() {
		return new CollisionResult(this.isIntersecting, this.willIntersect,
				this.isOverlap.neg(), this.willOverlap.neg(),
				this.otherMoveable, this.thisMoveable);
	}

	public boolean isOtherMoveable() {
		return this.otherMoveable;
	}

	public boolean isThisMoveable() {
		return this.thisMoveable;
	}

	public void print() {
		System.out.println("IsIntersecting: " + this.isIntersecting
				+ " isOverlap: " + this.isOverlap);
		System.out.println("WillIntersecting: " + this.willIntersect
				+ " willOverlap: " + this.willOverlap);
	}
}
