package com.googlecode.jumpnevolve.math;

public interface NextShape {

	public boolean isFinished();

	public Vector getCenter();

	public ConvexShape modifyCenter(Vector newCenter);

	public ConvexShape MoveCenter(Vector diff);

	public CollisionResult getCollision(NextShape other, Vector deltaVelocity);
}
