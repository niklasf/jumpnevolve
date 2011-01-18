/**
 * 
 */
package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

import sun.misc.FpUtils;

/**
 * @author Erik Wagner
 * 
 */
public class Polygon implements LineConsisting {

	private ArrayList<Vector> points = new ArrayList<Vector>();
	private ArrayList<PointLine> lines = new ArrayList<PointLine>();
	private float left, right, up, down;
	private Vector center;

	public Polygon(ArrayList<Vector> points) {
		for (Vector p : points) {
			this.addPoint(p);
		}
	}

	public void addPoint(Vector point) {
		int size = this.points.size();
		if (size > 2) {
			this.lines.remove(this.lines.size() - 1);
		}
		if (size >= 2) {
			this.lines.add(new PointLine(this.points
					.get(size - 1), point));
		}
		this.lines.add(new PointLine(point, this.points.get(0)));

		if (Float.isNaN(this.left)) {
			this.left = point.x;
		} else {
			this.left = Math.min(this.left, point.x);
		}
		if (Float.isNaN(this.right)) {
			this.right = point.x;
		} else {
			this.right = Math.max(this.right, point.x);
		}
		if (Float.isNaN(this.up)) {
			this.up = point.y;
		} else {
			this.up = Math.min(this.up, point.y);
		}
		if (Float.isNaN(this.down)) {
			this.down = point.y;
		} else {
			this.down = Math.max(this.down, point.y);
		}
		if (center == null) {
			this.center = point;
		} else {
			Vector newCenter = this.center.mul(size);
			newCenter = newCenter.add(point);
			this.center = newCenter.div(size + 1);
		}
		this.points.add(point);
	}

	@Override
	public boolean doesCollide(Shape other) {
		if (other instanceof Circle) {
			return other.doesCollide(this);
		} else if (other instanceof LineConsisting) {
			for (PointLine line : this.lines) {
				for (PointLine l : ((LineConsisting) other).getLines()) {
					if (line.crosses(l)) {
						return true;
					}
				}
			}
			if (this.isPointInThis(other.getCenter())) {
				return true;
			} else {
				return false;
			}
		} else {
			return other.getBestCircle().doesCollide(this);
		}
	}

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
		// TODO Auto-generated method stub
		return null;
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
			return this.getCenter().y - this.getUpperEnd();
		case Shape.DOWN:
			return this.getLowerEnd() - this.getCenter().y;
		case Shape.RIGHT:
			return this.getRightEnd() - this.getCenter().x;
		case Shape.LEFT:
			return this.getCenter().x - this.getLeftEnd();
		default:
			return 0;
		}
	}

	@Override
	public float getLeftEnd() {
		return this.left;
	}

	@Override
	public float getLowerEnd() {
		return this.down;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.math.Shape#getOverlap(com.googlecode.jumpnevolve
	 * .math.PointLine, com.googlecode.jumpnevolve.math.Vector)
	 */
	@Override
	public Vector getOverlap(PointLine line, Vector pointInOtherShape) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getRightEnd() {
		return this.right;
	}

	@Override
	public float getUpperEnd() {
		return this.up;
	}

	@Override
	public float getXRange() {
		return this.right - this.left;
	}

	@Override
	public float getYRange() {
		return this.down - this.up;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.googlecode.jumpnevolve.math.Shape#isIntersecting(com.googlecode.
	 * jumpnevolve.math.PointLine)
	 */
	@Override
	public boolean isIntersecting(PointLine line) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.googlecode.jumpnevolve.math.Shape#isPointInThis(com.googlecode.
	 * jumpnevolve.math.Vector)
	 */
	@Override
	public boolean isPointInThis(Vector p) {
		PointLine centerToPoint = new PointLine(this.getCenter(), p);
		for (Vector point : this.points) {
			if (centerToPoint.crosses(new PointLine(point, this.getCenter()))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Shape modifyCenter(Vector center) {
		ArrayList<Vector> newPoints = new ArrayList<Vector>();
		Vector diff = center.sub(this.getCenter());
		for (Vector point : points) {
			newPoints.add(point.add(diff));
		}
		return new Polygon(newPoints);
	}

	@Override
	public org.newdawn.slick.geom.Shape toSlickShape() {
		org.newdawn.slick.geom.Polygon poly = new org.newdawn.slick.geom.Polygon();
		for (Vector point : points) {
			poly.addPoint(point.x, point.y);
		}
		return poly;
	}

	@Override
	public PointLine[] getLines() {
		return (PointLine[]) this.lines.toArray();
	}

	@Override
	public Vector[] getPoints() {
		return (Vector[]) this.points.toArray();
	}

}
