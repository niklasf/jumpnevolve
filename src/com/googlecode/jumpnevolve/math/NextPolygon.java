package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

/**
 * @author Erik Wagner
 * 
 */
class NextPolygon implements ConvexShape {

	private ArrayList<Vector> points = new ArrayList<Vector>();
	private ArrayList<Vector> lines = new ArrayList<Vector>();
	private boolean finished = false;
	private Vector center = null;

	/**
	 * 
	 */
	public NextPolygon(ArrayList<Vector> points) {
		this((Vector[]) points.toArray());
	}

	public NextPolygon(Vector[] points) {
		for (Vector p : points) {
			this.addPoint(p);
		}
	}

	private NextPolygon(ArrayList<Vector> points, ArrayList<Vector> lines,
			Vector center) {
		this.points = points;
		this.lines = lines;
		this.center = center;
		this.finished = true;
	}

	public NextPolygon() {
	}

	public void addPoint(Vector point) {
		if (!finished) {
			this.points.add(point);
		}
	}

	public void finish() {
		if (this.points.size() >= 3) {
			this.finished = true;
			this.buildLines();
			this.createCenter();
		}
	}

	public boolean isFinished() {
		return this.finished;
	}

	public int getNumberOfPoints() {
		return this.points.size();
	}

	public ArrayList<Vector> getPoints() {
		return this.points;
	}

	private void buildLines() {
		Vector p1;
		Vector p2;
		int size = points.size();
		for (int i = 0; i < size - 1; i++) {
			p1 = this.points.get(i);
			p2 = this.points.get(i + 1);
			lines.add(p2.sub(p1));
		}
		this.lines.add(this.points.get(size - 1).sub(this.points.get(0)));
	}

	private void createCenter() {
		Vector c = Vector.ZERO;
		for (Vector point : this.points) {
			c.add(point);
		}
		this.center = c.div(this.points.size());
	}

	@Override
	public ConvexShape MoveCenter(Vector diff) {
		ArrayList<Vector> nextPoints = new ArrayList<Vector>();
		for (Vector vector : this.points) {
			nextPoints.add(vector.add(diff));
		}
		return new NextPolygon(nextPoints, this.lines, this.center.add(diff));
	}

	@Override
	public Vector getCenter() {
		if (this.center == null) {
			this.createCenter();
		}
		return this.center;
	}

	@Override
	public CollisionResult getCollision(NextShape other) {
		CollisionResult colRe = new CollisionResult();
		if (this.isFinished() && other.isFinished()) {
			if (other instanceof ConvexShape) {
				ConvexShape otherConvex = (ConvexShape) other;
				Vector[] thisAxises = this.getAxises(otherConvex);
				Vector[] otherAxises = otherConvex.getAxises(this);
				int thisLineCount = thisAxises.length;
				int otherLineCount = otherAxises.length;
				float minOverlapDistance = Float.POSITIVE_INFINITY;
				Vector translationAxis = Vector.ZERO;
				Vector curAxis;
				for (int i = 0; i < thisLineCount + otherLineCount; i++) {
					if (i < thisLineCount) {
						curAxis = thisAxises[i];
					} else {
						curAxis = otherAxises[i - thisLineCount];
					}
					AxisProjection thisProjection = this.projectOnAxis(curAxis);
					AxisProjection otherProjection = otherConvex
							.projectOnAxis(curAxis);
					float dist = getIntervalDistance(thisProjection.minimum,
							thisProjection.maximum, otherProjection.minimum,
							otherProjection.maximum);
					if (dist > 0) {
						colRe.setNotIntersecting();
						return colRe;
					} else {
						dist = -dist;
						if (dist < minOverlapDistance) {
							minOverlapDistance = dist;
							if (this.getCenter().sub(other.getCenter()).mul(
									curAxis) < 0) {
								translationAxis = curAxis.neg();
							} else {
								translationAxis = curAxis;
							}
						}
					}
				}
				colRe
						.setMinimumOverlap(translationAxis
								.mul(minOverlapDistance));
				return colRe;
			} else if (other instanceof NotConvexShape) {
				ConvexShape[] convexes = ((NotConvexShape) other)
						.toConvexShapes();
				CollisionResult[] results = new CollisionResult[convexes.length];
				for (int i = 0; i < convexes.length; i++) {
					results[i] = this.getCollision(convexes[i]);
				}
				Vector overlap = Vector.ZERO;
				int numberOfOverlaps = 0;
				for (CollisionResult result : results) {
					if (result.isIntersecting()) {
						overlap.add(result.getMinimumOverlap());
						numberOfOverlaps++;
					}
				}
				if (numberOfOverlaps > 0) {
					overlap = overlap.div(numberOfOverlaps);
					colRe.setMinimumOverlap(overlap);
					return colRe;
				} else {
					colRe.setNotIntersecting();
					return colRe;
				}
			} else {
				colRe.setNotIntersecting();
				return colRe;
			}
		} else {
			colRe.setNotIntersecting();
			return colRe;
		}
	}

	private float getIntervalDistance(float minA, float maxA, float minB,
			float maxB) {
		if (minA < minB) {
			return minB - maxA;
		} else {
			return minA - maxB;
		}
	}

	@Override
	public ConvexShape modifyCenter(Vector newCenter) {
		return this.MoveCenter(newCenter.sub(this.center));
	}

	@Override
	public AxisProjection projectOnAxis(Vector axis) {
		float dotProduct = axis.mul(this.points.get(0));
		float min = dotProduct;
		float max = dotProduct;
		for (int i = 1; i < this.points.size(); i++) {
			dotProduct = this.points.get(i).mul(axis);
			if (dotProduct < min) {
				min = dotProduct;
			} else {
				if (dotProduct > max) {
					max = dotProduct;
				}
			}
		}
		return new AxisProjection(min, max);
	}

	@Override
	public Vector[] getAxises(ConvexShape other) {
		Vector[] axises = new Vector[this.lines.size()];
		for (int i = 0; i < this.lines.size(); i++) {
			axises[i] = this.lines.get(i).rotateQuarterClockwise();
		}
		return axises;
	}
}
