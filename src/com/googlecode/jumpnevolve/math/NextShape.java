package com.googlecode.jumpnevolve.math;

public interface NextShape {

	public boolean isFinished();

	public Vector getCenter();

	public ConvexShape modifyCenter(Vector newCenter);

	public ConvexShape MoveCenter(Vector diff);

	public CollisionResult getCollision(NextShape other, Vector deltaVelocity,
			boolean thisMoveable, boolean otherMoveable);

	/**
	 * @return Die X-Koordinate des linken Endes des Objekts
	 */
	public float getLeftEnd();

	/**
	 * @return Die X-Koordinate des rechten Endes des Objekts
	 */
	public float getRightEnd();

	/**
	 * @return Die Y-Koordinate des oberen Endes des Objekts
	 */
	public float getUpperEnd();

	/**
	 * @return Die Y-Koordinate des unteren Endes des Objekts
	 */
	public float getLowerEnd();
}
