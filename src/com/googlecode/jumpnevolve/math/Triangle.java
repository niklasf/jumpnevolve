package com.googlecode.jumpnevolve.math;

import org.newdawn.slick.geom.Polygon;

/**
 * @author Erik Wagner
 * 
 */
public class Triangle implements LineConsisting {

	public final Vector[] points;
	public final PointLine[] lines;
	public final Vector center;

	/**
	 * 
	 */
	public Triangle(Vector p1, Vector p2, Vector p3) {
		this.points = new Vector[] { p1, p2, p3 };
		this.center = p1.add(p3.add(p2.sub(p3).div(2.0f)).sub(p1).mul(
				2.0f / 3.0f));
		this.lines = new PointLine[] { new PointLine(p1, p2),
				new PointLine(p2, p3), new PointLine(p1, p3) };
	}

	private Triangle(Vector[] points, Vector center) {
		this.points = points;
		this.center = center;
		this.lines = new PointLine[] { new PointLine(points[1], points[2]),
				new PointLine(points[2], points[3]),
				new PointLine(points[1], points[3]) };
	}

	@Override
	public boolean doesCollide(Shape other) {
		// TODO Auto-generated method stub
		if (other instanceof Triangle) {
			return isTriangleTriangleCollision((Triangle) other);
		}
		return false;
	}

	private boolean isTriangleTriangleCollision(Triangle other) {
		for (Line pointLine : this.lines) {
			for (Line pointLine2 : other.lines) {
				if (pointLine.crosses(pointLine2)) {
					return true;
				}
			}
		}
		if (other.isPointInThis(this.points[0])
				|| this.isPointInThis(other.points[0])) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Circle getBestCircle() {
		// FIXME Auto-generated method stub
		return null;
	}

	@Override
	public Vector getCenter() {
		return this.center;
	}

	@Override
	public Collision getCollision(Shape other, boolean otherMoveable,
			boolean thisMoveable) {
		// TODO Auto-generated method stub
		if (other instanceof Triangle) {
			return new Collision(this
					.getTriangleTriangleOverlap((Triangle) other),
					thisMoveable, otherMoveable);
		}
		return null;
	}

	private Vector getTriangleTriangleOverlap(Triangle other) {
		boolean[] cornerInsideThis = new boolean[3];
		boolean[] cornerInsideOther = new boolean[3];
		cornerInsideOther[0] = other.isPointInThis(this.points[0]);
		cornerInsideOther[1] = other.isPointInThis(this.points[1]);
		cornerInsideOther[2] = other.isPointInThis(this.points[2]);
		cornerInsideThis[0] = this.isPointInThis(other.points[0]);
		cornerInsideThis[1] = this.isPointInThis(other.points[1]);
		cornerInsideThis[2] = this.isPointInThis(other.points[2]);
		int numberOfCornersInsideThis = 0;
		for (boolean b : cornerInsideThis) {
			if (b) {
				numberOfCornersInsideThis++;
			}
		}
		int numberOfCornersInsideOther = 0;
		for (boolean b : cornerInsideOther) {
			if (b) {
				numberOfCornersInsideOther++;
			}
		}
		if (numberOfCornersInsideOther == 3 || numberOfCornersInsideThis == 3) {
			return Vector.ZERO;
		}
		if (numberOfCornersInsideOther == 0 && numberOfCornersInsideThis == 0) {
			return Vector.ZERO;
		}
		switch (numberOfCornersInsideThis) {
		case 0:
			switch (numberOfCornersInsideOther) {
			case 1:
				PointLine line1,
				compare1;
				Vector point;
				if (cornerInsideOther[0]) {
					point = this.points[0];
					compare1 = this.lines[0];
				} else if (cornerInsideOther[1]) {
					point = this.points[1];
					compare1 = this.lines[1];
				} else {
					point = this.points[2];
					compare1 = this.lines[0];
				}
				if (other.lines[0].crosses(compare1)) {
					line1 = other.lines[0];
				} else if (other.lines[1].crosses(compare1)) {
					line1 = other.lines[1];
				} else {
					line1 = other.lines[2];
				}
				return point.sub(line1.getCrossingPoint(new PointLine(line1.p1,
						line1.p1.sub(line1.p2).rotateQuarterClockwise())));
			case 2:
				PointLine line,
				compare;
				Vector point1,
				point2;
				if (cornerInsideOther[0] && cornerInsideOther[1]) {
					point1 = this.points[0];
					point2 = this.points[1];
					compare = this.lines[1];
					if (other.lines[0].crosses(compare)) {
						line = other.lines[0];
					} else if (other.lines[1].crosses(compare)) {
						line = other.lines[1];
					} else {
						line = other.lines[2];
					}
				} else if (cornerInsideOther[0] && cornerInsideOther[2]) {
					point1 = this.points[0];
					point2 = this.points[2];
					compare = this.lines[1];
					if (other.lines[0].crosses(compare)) {
						line = other.lines[0];
					} else if (other.lines[1].crosses(compare)) {
						line = other.lines[1];
					} else {
						line = other.lines[2];
					}
				} else {
					point1 = this.points[2];
					point2 = this.points[1];
					compare = this.lines[0];
					if (other.lines[0].crosses(compare)) {
						line = other.lines[0];
					} else if (other.lines[1].crosses(compare)) {
						line = other.lines[1];
					} else {
						line = other.lines[2];
					}
				}
				Vector crossPoint = line
						.getCrossingPoint(new PointLine(line.p1, line.p1.sub(
								line.p2).rotateQuarterClockwise()));
				return Vector.max(point1.sub(crossPoint), point2
						.sub(crossPoint));
			}
			break;
		case 1:
			switch (numberOfCornersInsideOther) {
			case 0:
				return other.getTriangleTriangleOverlap(this);
			case 1:
				Vector thisPoint,
				otherPoint;
				if (cornerInsideOther[0]) {
					thisPoint = this.points[0];
				} else if (cornerInsideOther[1]) {
					thisPoint = this.points[1];
				} else {
					thisPoint = this.points[2];
				}
				if (cornerInsideThis[0]) {
					otherPoint = other.points[0];
				} else if (cornerInsideThis[1]) {
					otherPoint = other.points[1];
				} else {
					otherPoint = other.points[2];
				}
				return thisPoint.sub(otherPoint);
			case 2:
				Vector point;
				PointLine line;
				if (cornerInsideThis[0]) {
					point = other.points[0];
				} else if (cornerInsideThis[1]) {
					point = other.points[1];
				} else {
					point = other.points[2];
				}
				if (cornerInsideOther[0] && cornerInsideOther[1]) {
					line = this.lines[0];
				} else if (cornerInsideOther[0] && cornerInsideOther[2]) {
					line = this.lines[2];
				} else {
					line = this.lines[1];
				}
				Vector crossPoint = line
						.getCrossingPoint(new PointLine(line.p1, line.p1.sub(
								line.p2).rotateQuarterClockwise()));
				return crossPoint.sub(point);
			}
			break;
		case 2:
			switch (numberOfCornersInsideOther) {
			case 0:
				return other.getTriangleTriangleOverlap(this);
			case 1:
				return other.getTriangleTriangleOverlap(this);
			case 2:
				return Vector.ZERO;
			}
			break;
		default:
			break;
		}
		return Vector.ZERO;
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
		return !(centerToPoint.crosses(new PointLine(this.points[0],
				this.points[1]))
				|| centerToPoint.crosses(new PointLine(this.points[2],
						this.points[1])) || centerToPoint
				.crosses(new PointLine(this.points[0], this.points[2])));
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
	public Vector getOverlap(PointLine line, Vector pointInOtherShape) {
		Vector dir = line.getDistanceVectorTo(this.getCenter());
		if (!line.arePointsOnTheSameSide(this.getCenter(), pointInOtherShape)) {
			dir = dir.neg();
		}
		return line.getDistanceVectorTo(this.getCorner(dir));
	}

	@Override
	public boolean isIntersecting(PointLine line) {
		for (Line pointLine : this.lines) {
			if (line.crosses(pointLine)) {
				return true;
			}
		}
		return this.isPointInThis(line.p1);
	}

	private Vector getCorner(Vector direction) {
		if (direction.ang(this.points[0].sub(this.center)) < direction
				.ang(this.points[1].sub(this.center))) {
			if (direction.ang(this.points[0].sub(this.center)) < direction
					.ang(this.points[2].sub(this.center))) {
				return this.points[0];
			} else {
				return this.points[2];
			}
		} else {
			if (direction.ang(this.points[1].sub(this.center)) < direction
					.ang(this.points[2].sub(this.center))) {
				return this.points[1];
			} else {
				return this.points[2];
			}
		}
	}

	@Override
	public PointLine[] getLines() {
		return this.lines;
	}

	@Override
	public Vector[] getPoints() {
		return this.points;
	}
}
