package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

/**
 * @author Erik Wagner
 * 
 */
class NextPolygon implements ConvexShape {

	private ArrayList<Vector> relativePoints = new ArrayList<Vector>();
	private ArrayList<Vector> axises = new ArrayList<Vector>();
	private boolean finished = false;
	private final Vector center;
	private float left = Float.POSITIVE_INFINITY,
			right = Float.NEGATIVE_INFINITY, up = Float.POSITIVE_INFINITY,
			down = Float.NEGATIVE_INFINITY;

	/**
	 * 
	 */
	public NextPolygon(Vector center, ArrayList<Vector> relativePoints) {
		this(center, (Vector[]) relativePoints.toArray());
	}

	public NextPolygon(Vector center, Vector[] relativePoints) {
		for (Vector p : relativePoints) {
			this.addPoint(p);
		}
		this.center = center;
	}

	private NextPolygon(Vector center, ArrayList<Vector> relativePoints,
			ArrayList<Vector> lines, float left, float right, float up,
			float down) {
		this.relativePoints = relativePoints;
		this.axises = lines;
		this.center = center;
		this.finished = true;
	}

	public NextPolygon(Vector center) {
		this.center = center;
	}

	public void addPoint(Vector point) {
		if (!finished) {
			this.relativePoints.add(point);
			this.left = Math.min(point.x, this.left);
			this.right = Math.max(point.x, this.right);
			this.up = Math.min(point.x, this.up);
			this.down = Math.max(point.x, this.down);
		}
	}

	public void finish() {
		if (this.relativePoints.size() >= 3) {
			this.finished = true;
			this.buildAxises();
		}
	}

	public boolean isFinished() {
		return this.finished;
	}

	public int getNumberOfPoints() {
		return this.relativePoints.size();
	}

	public ArrayList<Vector> getRelativePoints() {
		return this.relativePoints;
	}

	public ArrayList<Vector> getPoints() {
		ArrayList<Vector> re = new ArrayList<Vector>();
		for (Vector vector : this.relativePoints) {
			re.add(vector.add(this.center));
		}
		return re;
	}

	private void buildAxises() {
		Vector p1;
		Vector p2;
		int size = relativePoints.size();
		for (int i = 0; i < size - 1; i++) {
			p1 = this.relativePoints.get(i);
			p2 = this.relativePoints.get(i + 1);
			axises.add(p2.sub(p1).rotateQuarterClockwise().getDirection());
		}
		this.axises.add(this.relativePoints.get(size - 1).sub(
				this.relativePoints.get(0)).rotateQuarterClockwise()
				.getDirection());
	}

	@Override
	public ConvexShape MoveCenter(Vector diff) {
		return new NextPolygon(this.center.add(diff), this.relativePoints,
				this.axises, this.left + diff.x, this.right + diff.x, this.up
						+ diff.y, this.down + diff.y);
	}

	@Override
	public Vector getCenter() {
		return this.center;
	}

	@Override
	public CollisionResult getCollision(NextShape other, Vector deltaVelocity,
			boolean thisMoveable, boolean otherMoveable) {
		CollisionResult colRe = new CollisionResult(thisMoveable, otherMoveable);
		if (this.isFinished() && other.isFinished()) {
			if (other instanceof ConvexShape) {
				ConvexShape otherConvex = (ConvexShape) other;
				ArrayList<Vector> thisAxises = this.getAxises(otherConvex);
				ArrayList<Vector> otherAxises = otherConvex.getAxises(this);
				int thisLineCount = thisAxises.size();
				int otherLineCount = otherAxises.size();
				float minOverlapDistanceIs = Float.POSITIVE_INFINITY;
				float minOverlapDistanceWill = Float.POSITIVE_INFINITY;
				Vector translationAxisIs = Vector.ZERO;
				Vector translationAxisWill = Vector.ZERO;
				Vector curAxis;
				for (int i = 0; i < thisLineCount + otherLineCount; i++) {
					if (i < thisLineCount) {
						curAxis = thisAxises.get(i);
					} else {
						curAxis = otherAxises.get(i - thisLineCount);
					}
					AxisProjection thisProjection = this.projectOnAxis(curAxis);
					AxisProjection otherProjection = otherConvex
							.projectOnAxis(curAxis);
					float distIs = getIntervalDistance(thisProjection.minimum,
							thisProjection.maximum, otherProjection.minimum,
							otherProjection.maximum);
					float velAdd = curAxis.mul(deltaVelocity);
					float distWill = getIntervalDistance(thisProjection.minimum
							+ velAdd, thisProjection.maximum + velAdd,
							otherProjection.minimum, otherProjection.maximum);
					if (distIs > 0 && distWill > 0) {
						colRe.setNotIntersecting();
						colRe.setWillNotIntersect();
						return colRe;
					} else {
						distIs = -distIs;
						distWill = -distWill;
						if (distIs < minOverlapDistanceIs) {
							minOverlapDistanceIs = distIs;
							if (this.getCenter().sub(other.getCenter()).mul(
									curAxis) < 0) {
								translationAxisIs = curAxis.neg();
							} else {
								translationAxisIs = curAxis;
							}
						}
						if (distWill < minOverlapDistanceWill) {
							minOverlapDistanceWill = distWill;
							if (this.getCenter().sub(other.getCenter()).mul(
									curAxis) < 0) {
								translationAxisWill = curAxis.neg();
							} else {
								translationAxisWill = curAxis;
							}
						}
					}
				}
				colRe.setIsOverlap(translationAxisIs.mul(minOverlapDistanceIs));
				colRe.setWillOverlap(translationAxisWill
						.mul(minOverlapDistanceWill));
				return colRe;
			} else if (other instanceof NotConvexShape) {
				ConvexShape[] convexes = ((NotConvexShape) other)
						.toConvexShapes();
				CollisionResult[] results = new CollisionResult[convexes.length];
				for (int i = 0; i < convexes.length; i++) {
					results[i] = this.getCollision(convexes[i], deltaVelocity,
							thisMoveable, otherMoveable);
				}
				return getUnionedResult(results, thisMoveable, otherMoveable);
			} else {
				colRe.setNotIntersecting();
				colRe.setWillNotIntersect();
				return colRe;
			}
		} else {
			colRe.setNotIntersecting();
			colRe.setWillNotIntersect();
			return colRe;
		}
	}

	private static CollisionResult getUnionedResult(CollisionResult[] results,
			boolean thisMoveable, boolean otherMoveable) {
		CollisionResult colRe = new CollisionResult(thisMoveable, otherMoveable);
		Vector overlapIs = Vector.ZERO, overlapWill = Vector.ZERO;
		int numberOfOverlapsIs = 0, numberOfOverlapsWill = 0;
		for (CollisionResult result : results) {
			if (result.isIntersecting()) {
				overlapIs.add(result.getIsOverlap());
				numberOfOverlapsIs++;
			}
			if (result.willIntersect()) {
				overlapWill.add(result.getWillOverlap());
				numberOfOverlapsWill++;
			}
		}
		if (numberOfOverlapsIs > 0) {
			overlapIs = overlapIs.div(numberOfOverlapsIs);
			colRe.setIsOverlap(overlapIs);
		} else {
			colRe.setNotIntersecting();
		}
		if (numberOfOverlapsWill > 0) {
			overlapWill = overlapWill.div(numberOfOverlapsWill);
			colRe.setWillOverlap(overlapWill);
		} else {
			colRe.setWillNotIntersect();
		}
		return colRe;
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
		float dotProduct = axis.mul(this.relativePoints.get(0));
		float min = dotProduct;
		float max = dotProduct;
		for (int i = 1; i < this.relativePoints.size(); i++) {
			dotProduct = this.relativePoints.get(i).mul(axis);
			if (dotProduct < min) {
				min = dotProduct;
			} else {
				if (dotProduct > max) {
					max = dotProduct;
				}
			}
		}
		float centerProduct = axis.mul(this.center);
		return new AxisProjection(min + centerProduct, max + centerProduct);
	}

	@Override
	public ArrayList<Vector> getAxises(ConvexShape other) {
		return this.axises;
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
	public float getRightEnd() {
		return this.right;
	}

	@Override
	public float getUpperEnd() {
		return this.up;
	}
}
