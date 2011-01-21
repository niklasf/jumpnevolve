package com.googlecode.jumpnevolve.math;

/**
 * Neue allgemeine Klasse für geometrische Figuren
 * 
 * @author Erik Wagner
 * 
 */
interface ConvexShape extends NextShape {

	public AxisProjection projectOnAxis(Vector axis);

	public Vector[] getAxises(ConvexShape other);
}
