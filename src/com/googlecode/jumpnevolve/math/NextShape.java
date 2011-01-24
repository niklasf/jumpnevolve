package com.googlecode.jumpnevolve.math;

public interface NextShape {

	public boolean isFinished();

	public Vector getCenter();

	public HelpRectangle getBoundingRect();

	public ConvexShape modifyCenter(Vector newCenter);

	public ConvexShape moveCenter(Vector diff);

	public CollisionResult getCollision(NextShape other, Vector deltaVelocity,
			boolean thisMoveable, boolean otherMoveable);

	public boolean isPointIn(Vector point);

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

	/**
	 * @return Eine Slick Version dieser Form.
	 */
	public org.newdawn.slick.geom.Shape toSlickShape();

	/**
	 * Skaliert dieses Objekt zu einem Zoom
	 * 
	 * @param zoom
	 *            Der Zoom zum Skalieren
	 * @return Das skalierte Shape
	 */
	public NextShape scale(float zoom);
}
