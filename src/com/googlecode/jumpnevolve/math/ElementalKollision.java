package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 * 
 */
public class ElementalKollision {

	private final Vector restoring;
	private final Vector overlap;

	/**
	 * 
	 */
	public ElementalKollision(Vector restoring, Vector overlap) {
		this.restoring = restoring;
		this.overlap = overlap;
		// Kontrolle, dass Overlap entgegen Restoring zeigt
		if (restoring.ang(overlap) < Math.PI) {
			System.out.println("Falsche Vektoren");
		}
	}

	public ElementalKollision invertKollision() {
		return new ElementalKollision(this.overlap.add(this.restoring),
				this.overlap.neg());
	}

	public Vector getRestoring() {
		return this.restoring;
	}
}
