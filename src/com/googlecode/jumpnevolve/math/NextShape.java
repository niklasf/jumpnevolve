package com.googlecode.jumpnevolve.math;

/**
 * Neue allgemeine Klasse für geometrische Figuren
 * 
 * @author Erik Wagner
 * 
 */
public interface NextShape {

	public Vector getCenter();

	public NextShape modifyCenter(Vector newCenter);

	public NextShape MoveCenter(Vector diff);

	public CollisionResult getCollision(NextShape other);

	public AxisProjection projectOnAxis(Vector axis);
}
