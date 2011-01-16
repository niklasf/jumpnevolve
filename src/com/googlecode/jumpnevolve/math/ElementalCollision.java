package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 * 
 */
public class ElementalCollision {

	private final Vector restoring;
	private final Vector overlap;

	/**
	 * 
	 */
	public ElementalCollision(boolean thisMoveable, boolean otherMoveable,
			Vector overlap) {
		this.restoring = this.toRestoring(overlap, thisMoveable, otherMoveable);
		this.overlap = overlap;
	}

	public ElementalCollision() {
		this(true, true, Vector.ZERO);
	}

	private ElementalCollision(Vector restoring, Vector overlap) {
		this.restoring = restoring;
		this.overlap = overlap;
	}

	private Vector toRestoring(Vector overlap, boolean thisMoveable,
			boolean otherMoveable) {
		if (otherMoveable == thisMoveable) {
			return overlap.div(-2.0f);
		} else if (otherMoveable) {
			return Vector.ZERO;
		} else {
			return overlap.neg();
		}
	}

	public ElementalCollision invertKollision() {
		return new ElementalCollision(this.overlap.add(this.restoring),
				this.overlap.neg());
	}

	public Vector getRestoring() {
		return this.restoring;
	}
}
