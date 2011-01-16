/**
 * 
 */
package com.googlecode.jumpnevolve.math;

import org.newdawn.slick.geom.Polygon;

/**
 * @author Erik Wagner
 * 
 */
public class Triangle implements Shape {

	public final Vector[] points;
	public final Vector center;

	/**
	 * 
	 */
	public Triangle(Vector p1, Vector p2, Vector p3) {
		// TODO Auto-generated constructor stub
		this.points = new Vector[] { p1, p2, p3 };
		this.center = p1.add(p3.add(p2.sub(p3).div(2.0f)).sub(p1).mul(
				2.0f / 3.0f));
	}

	private Triangle(Vector[] points, Vector center) {
		this.points = points;
		this.center = center;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.math.Shape#doesCollide(com.googlecode.jumpnevolve
	 * .math.Shape)
	 */
	@Override
	public boolean doesCollide(Shape other) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.jumpnevolve.math.Shape#getBestCircle()
	 */
	@Override
	public Circle getBestCircle() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.jumpnevolve.math.Shape#getCenter()
	 */
	@Override
	public Vector getCenter() {
		return this.center;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.math.Shape#getCollision(com.googlecode.jumpnevolve
	 * .math.Shape, boolean, boolean)
	 */
	@Override
	public Collision getCollision(Shape other, boolean otherMoveable,
			boolean thisMoveable) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.jumpnevolve.math.Shape#getDistanceToSide(byte)
	 */
	@Override
	public float getDistanceToSide(byte direction) {
		switch (direction) {
		case Shape.UP:
			return this.center.y - this.getUpperEnd();
		case Shape.DOWN:
			return this.getLowerEnd() - this.center.y;
		case Shape.RIGHT:
			return this.getRightEnd() - this.center.x;
		case Shape.LEFT:
			return this.center.x - this.getLeftEnd();
		default:
			return 0;
		}
	}

	@Override
	public float getLeftEnd() {
		return Math.min(Math.min(this.points[0].x, this.points[1].x),
				this.points[2].x);
	}

	@Override
	public float getLowerEnd() {
		return Math.max(Math.max(this.points[0].y, this.points[1].y),
				this.points[2].y);
	}

	@Override
	public float getRightEnd() {
		return Math.max(Math.max(this.points[0].x, this.points[1].x),
				this.points[2].x);
	}

	@Override
	public float getUpperEnd() {
		return Math.min(Math.min(this.points[0].y, this.points[1].y),
				this.points[2].y);
	}

	@Override
	public float getXRange() {
		return this.getRightEnd() - this.getLeftEnd();
	}

	@Override
	public float getYRange() {
		return this.getLowerEnd() - this.getUpperEnd();
	}

	@Override
	public boolean isPointInThis(Vector p) {
		PointLine centerToPoint = new PointLine(this.getCenter(), p);
		return !(centerToPoint.crossesLine(new PointLine(this.points[0],
				this.points[1]))
				|| centerToPoint.crossesLine(new PointLine(this.points[2],
						this.points[1])) || centerToPoint
				.crossesLine(new PointLine(this.points[0], this.points[2])));
	}

	@Override
	public Shape modifyCenter(Vector center) {
		Vector diff = center.sub(this.getCenter());
		return new Triangle(new Vector[] { this.points[0].add(diff),
				this.points[1].add(diff), this.points[2].add(diff) }, center);
	}

	@Override
	public org.newdawn.slick.geom.Shape toSlickShape() {
		Polygon re = new Polygon();
		re.addPoint(points[0].x, points[0].y);
		re.addPoint(points[1].x, points[1].y);
		re.addPoint(points[2].x, points[2].y);
		return re;
	}

	@Override
	public ElementalCollision getElementalCollision(Shape other,
			boolean otherMoveable, boolean thisMoveable) {
		// TODO Auto-generated method stub
		return null;
	}
}
