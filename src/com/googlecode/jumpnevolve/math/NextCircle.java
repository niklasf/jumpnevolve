package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

/**
 * @author Erik Wagner
 * 
 */
class NextCircle implements ConvexShape {

	public final float radius;
	public final Vector center;

	/**
	 * 
	 */
	public NextCircle(Vector center, float radius) {
		this.center = center;
		this.radius = Math.abs(radius);
	}

	@Override
	public ConvexShape MoveCenter(Vector diff) {
		return this.modifyCenter(this.center.add(diff));
	}

	@Override
	public Vector getCenter() {
		return this.center;
	}

	@Override
	public CollisionResult getCollision(NextShape other, Vector deltaVelocity) {
		CollisionResult colRe = new CollisionResult();
		if (other instanceof NextCircle) {
			float dist = other.getCenter().getDistance(this.center);
			float radsum = this.radius + ((NextCircle) other).radius;
			if (dist > radsum) {
				colRe.setNotIntersecting();
				return colRe;
			} else {
				colRe.setIsOverlap(this.center.sub(other.getCenter()).mul(
						radsum - dist));
				return colRe;
			}
		} else if (other instanceof NextPolygon) {
			return other.getCollision(this, deltaVelocity.neg()).invert();
		}
		colRe.setNotIntersecting();
		return colRe;
	}

	@Override
	public ConvexShape modifyCenter(Vector newCenter) {
		return new NextCircle(newCenter, this.radius);
	}

	@Override
	public AxisProjection projectOnAxis(Vector axis) {
		float d = this.getCenter().mul(axis);
		return new AxisProjection(d - this.radius, d + this.radius);
	}

	@Override
	public Vector[] getAxises(ConvexShape other) {
		if (other instanceof NextPolygon) {
			NextPolygon poly = (NextPolygon) other;
			ArrayList<Vector> points = poly.getPoints();
			Vector[] axises = new Vector[points.size()];
			for (int i = 0; i < points.size(); i++) {
				axises[i] = points.get(i).sub(this.center);
			}
			return axises;
		} else {
			return new Vector[] { other.getCenter().sub(this.getCenter()) };
		}
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
