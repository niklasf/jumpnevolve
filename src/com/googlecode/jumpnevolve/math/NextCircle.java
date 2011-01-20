package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 * 
 */
public class NextCircle implements NextShape {

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
	public NextShape MoveCenter(Vector diff) {
		return this.modifyCenter(this.center.add(diff));
	}

	@Override
	public Vector getCenter() {
		return this.center;
	}

	@Override
	public CollisionResult getCollision(NextShape other) {
		CollisionResult colRe = new CollisionResult();
		if (other instanceof NextCircle) {
			float dist = other.getCenter().getDistance(this.center);
			float radsum = this.radius + ((NextCircle) other).radius;
			if (dist > radsum) {
				colRe.setNotIntersecting();
				return colRe;
			} else {
				colRe.setMinimumOverlap(this.center.sub(other.getCenter()).mul(
						radsum - dist));
				return colRe;
			}
		} else if (other instanceof NextPolygon) {
			return other.getCollision(this).invert();
		}
		colRe.setNotIntersecting();
		return colRe;
	}

	@Override
	public NextShape modifyCenter(Vector newCenter) {
		return new NextCircle(newCenter, this.radius);
	}

	@Override
	public AxisProjection projectOnAxis(Vector axis) {
		float d = this.getCenter().mul(axis);
		return new AxisProjection(d - this.radius, d + this.radius);
	}

}
