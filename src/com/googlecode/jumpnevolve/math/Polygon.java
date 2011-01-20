/**
 * 
 */
package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

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
		this((Vector[]) points.toArray());
	}

	public Polygon(Vector[] points) {
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
			this.lines.add(new PointLine(this.points.get(size - 1), point));
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

	public Rectangle getBoundingRect() {
		return new Rectangle(this.getLeftEnd(), this.getUpperEnd(), this
				.getXRange(), this.getYRange());
	}

	@Override
	public boolean doesCollide(Shape other) {
		if (this.getBoundingRect().doesCollide(other)) {
			if (other instanceof Circle) {
				for (PointLine line : this.lines) {
					if (other.isIntersecting(line)) {
						return true;
					}
				}
				return false;
			} else if (other instanceof LineConsisting) {
				for (PointLine line : this.lines) {
					for (PointLine l : ((LineConsisting) other).toPolygon().lines) {
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
				return this.doesCollide(other.getBestCircle());
			}
		} else {
			return false;
		}
	}

	@Override
	public Circle getBestCircle() {
		// TODO Auto-generated method stub
		return this.getBoundingRect().getBestCircle();
	}

	@Override
	public Vector getCenter() {
		return this.center;
	}

	@Override
	public Collision getCollision(Shape other, boolean otherMoveable,
			boolean thisMoveable) {
		Collision col = new Collision(thisMoveable);
		for (PointLine line : this.lines) {
			if (other.isIntersecting(line)) {
				col.addOverlap(other.getOverlap(line, this.getCenter()),
						otherMoveable);
			}
		}
		return col;
	}

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

	@Override
	public Vector getOverlap(PointLine line, Vector pointInOtherShape) {
		// TODO Auto-generated method stub
		Vector overlap = null;
		for (Vector point : this.points) {
			if (MathUtils.arePointsInTheSameRayRect(point, pointInOtherShape,
					line)) {
				if (overlap == null) {
					overlap = line.getDistanceVectorTo(point);
				} else {
					overlap = Vector.max(overlap, line
							.getDistanceVectorTo(point));
				}
			}
		}
		if (overlap == null) {
			for (PointLine line2 : this.lines) {
				if (MathUtils.isLineInRayRect(line2, line, pointInOtherShape)) {
					if (overlap == null) {
						overlap = MathUtils.getMaxDistanceToLineInRayRect(
								line2, line, pointInOtherShape);
					} else {
						overlap = Vector.max(overlap, MathUtils
								.getMaxDistanceToLineInRayRect(line2, line,
										pointInOtherShape));
					}
				}
			}
		}
		return overlap;
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

	@Override
	public boolean isIntersecting(PointLine line) {
		if (this.isPointInThis(line.p1) || this.isPointInThis(line.p2)) {
			return true;
		} else {
			for (PointLine line2 : this.lines) {
				if (line.crosses(line2)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isPointInThis(Vector p) {
		// Angewendet wird die allgemeine Strahlen-Methode
		// (siehe http://rw7.de/ralf/inffaq/polygon.html#strahl)
		Ray testRay = new Ray(p, Vector.RIGHT);
		int n = 0;
		for (PointLine line : this.lines) {
			if (testRay.crosses(line)) {
				n++;
			}
		}
		return n % 2 == 1;
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
	public Polygon toPolygon() {
		return this;
	}

	@Override
	public boolean isCompletlyIn(PointLine line) {
		if (this.isPointInThis(line.p1) && this.isPointInThis(line.p2)) {
			for (PointLine line2 : this.lines) {
				if (line.crosses(line2)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
