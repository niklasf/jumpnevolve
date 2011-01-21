package com.googlecode.jumpnevolve.math;

/**
 * @author e.wagner
 * 
 */
interface NotConvexShape extends NextShape {

	/**
	 * Zerlegt diese nicht konvexe Form in mehrere konvexe Formen
	 * 
	 * @return Ein Array aus ConvexShapes
	 */
	public ConvexShape[] toConvexShapes();
}
