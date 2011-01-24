package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

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
	public ConvexShape moveCenter(Vector diff) {
		return this.modifyCenter(this.center.add(diff));
	}

	@Override
	public Vector getCenter() {
		return this.center;
	}

	@Override
	public CollisionResult getCollision(NextShape other, Vector deltaVelocity,
			boolean thisMoveable, boolean otherMoveable) {
		CollisionResult colRe = new CollisionResult(thisMoveable, otherMoveable);
		if (other instanceof NextCircle) {
			float dist = other.getCenter().getDistance(this.center);
			float radsum = this.radius + ((NextCircle) other).radius;
			if (dist > radsum) {
				colRe.setNotIntersecting();
			} else {
				colRe.setIsOverlap(this.center.sub(other.getCenter())
						.getDirection().mul(radsum - dist));
			}
			Vector nextCenter = this.center.add(deltaVelocity);
			dist = nextCenter.getDistance(other.getCenter());
			if (dist > radsum) {
				colRe.setWillNotIntersect();
			} else {
				colRe.setWillOverlap(nextCenter.sub(other.getCenter())
						.getDirection().mul(radsum - dist));
			}
			return colRe;
		} else if (other instanceof NextPolygon) {
			return other.getCollision(this, deltaVelocity.neg(), otherMoveable,
					thisMoveable).invert();
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
	public ArrayList<Vector> getAxises(ConvexShape other) {
		if (other instanceof NextPolygon) {
			NextPolygon poly = (NextPolygon) other;
			ArrayList<Vector> points = poly.getRelativePoints();
			Vector diffedCenter = poly.getCenter().sub(this.center);
			ArrayList<Vector> axises = new ArrayList<Vector>();
			for (int i = 0; i < points.size(); i++) {
				axises.add(points.get(i).sub(diffedCenter).getDirection());
			}
			return axises;
		} else {
			ArrayList<Vector> axises = new ArrayList<Vector>();
			axises.add(other.getCenter().sub(this.center).getDirection());
			return axises;
		}
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public float getLeftEnd() {
		return this.center.x - this.radius;
	}

	@Override
	public float getLowerEnd() {
		return this.center.y + this.radius;
	}

	@Override
	public float getRightEnd() {
		return this.center.x + this.radius;
	}

	@Override
	public float getUpperEnd() {
		return this.center.y - this.radius;
	}

	@Override
	public Shape toSlickShape() {
		return new Circle(this.center.x, this.center.y, this.radius);
	}

	@Override
	public NextShape scale(float zoom) {
		return new NextCircle(this.center, this.radius * zoom);
	}

	@Override
	public boolean isPointIn(Vector point) {
		return this.center.getDistance(point) <= this.radius;
	}

	@Override
	public String toString() {
		return "Circle --> Center: " + this.center + " Radius: " + this.radius;
	}

	@Override
	public HelpRectangle getBoundingRect() {
		return new HelpRectangle(this.getLeftEnd(), this.getRightEnd(), this
				.getUpperEnd(), this.getLowerEnd());
	}
}
