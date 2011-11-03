package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

/**
 * @author Erik Wagner
 * 
 */
class NextPolygon implements ConvexShape {

	private ArrayList<Vector> relativePoints = new ArrayList<Vector>();
	private ArrayList<Vector> points;
	private ArrayList<Vector> axises = new ArrayList<Vector>();
	private ArrayList<PointLine> relativeLines;
	private boolean finished = false;
	private final Vector center;
	private float left = Float.POSITIVE_INFINITY,
			right = Float.NEGATIVE_INFINITY, up = Float.POSITIVE_INFINITY,
			down = Float.NEGATIVE_INFINITY;
	private HelpRectangle boundingRect;

	/**
	 *
	 */
	public NextPolygon(Vector center, ArrayList<Vector> relativePoints) {
		this(center, (Vector[]) relativePoints.toArray());
	}

	public NextPolygon(Vector center, Vector[] relativePoints) {
		for (Vector p : relativePoints) {
			this.addRelativePoint(p);
		}
		this.center = center;
	}

	private NextPolygon(Vector center, ArrayList<Vector> relativePoints,
			ArrayList<Vector> axises, float left, float right, float up,
			float down, HelpRectangle boundingRect) {
		this.relativePoints = relativePoints;
		this.axises = axises;
		this.center = center;
		this.finished = true;
		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
		this.boundingRect = boundingRect;
	}

	public NextPolygon(Vector center) {
		this.center = center;
	}

	public void addRelativePoint(Vector point) {
		if (!(finished || this.relativePoints.contains(point))) {
			this.relativePoints.add(point);
			this.left = Math.min(point.x, this.left);
			this.right = Math.max(point.x, this.right);
			this.up = Math.min(point.y, this.up);
			this.down = Math.max(point.y, this.down);
			this.boundingRect = null;
			this.points = null;
			this.relativeLines = null;
		}
	}

	public void finish() {
		if (this.relativePoints.size() >= 3) {
			this.finished = true;
			this.buildAxises();
			this.left += this.center.x;
			this.right += this.center.x;
			this.up += this.center.y;
			this.down += this.center.y;
		} else {
			System.out.println("Cannot finish polygon (" + super.toString()
					+ ") with less than two points");
		}
	}

	public int getNumberOfPoints() {
		return this.relativePoints.size();
	}

	public ArrayList<Vector> getRelativePoints() {
		return this.relativePoints;
	}

	public ArrayList<Vector> getPoints() {
		if (this.points == null) {
			this.buildPoints();
		}
		return this.points;
	}

	public ArrayList<PointLine> getRelativeLines() {
		if (this.relativeLines == null) {
			this.buildRelativeLines();
		}
		return relativeLines;
	}

	private void buildRelativeLines() {
		this.relativeLines = new ArrayList<PointLine>();
		for (int i = 1; i < this.relativePoints.size(); i++) {
			this.relativeLines
					.add(new PointLine(this.relativePoints.get(i - 1),
							this.relativePoints.get(i)));
		}
		this.relativeLines.add(new PointLine(this.relativePoints
				.get(this.relativePoints.size() - 1), this.relativePoints
				.get(0)));
	}

	private void buildPoints() {
		this.points = new ArrayList<Vector>();
		for (Vector vector : this.relativePoints) {
			this.points.add(vector.add(this.center));
		}
	}

	private void buildAxises() {
		Vector p1;
		Vector p2;
		Vector axis;
		int size = relativePoints.size();
		for (int i = 0; i < size - 1; i++) {
			p1 = this.relativePoints.get(i);
			p2 = this.relativePoints.get(i + 1);
			axis = p2.sub(p1).rotateQuarterClockwise().getDirection();
			if (!this.axises.contains(axis)) {
				axises.add(axis);
			}
		}
		axis = this.relativePoints.get(size - 1)
				.sub(this.relativePoints.get(0)).rotateQuarterClockwise()
				.getDirection();
		if (!this.axises.contains(axis)) {
			axises.add(axis);
		}
	}

	private void buildBoundingRect() {
		this.boundingRect = new HelpRectangle(this.left, this.right, this.up,
				this.down);
	}

	@Override
	public CollisionResult getCollision(NextShape other, Vector deltaVelocity,
			boolean thisMoveable, boolean otherMoveable) {
		CollisionResult colRe = new CollisionResult(thisMoveable, otherMoveable);
		if (this.isFinished() && other.isFinished()) {
			if (!(other.getBoundingRect().doesCollide(this.getBoundingRect()))) {
				colRe.setNotIntersecting();
				if (!(other.getBoundingRect().doesCollide(this
						.getBoundingRect().moveCenter(deltaVelocity)))) {
					colRe.setWillNotIntersect();
					return colRe;
				}
			} else if (!(other.getBoundingRect().doesCollide(this
					.getBoundingRect().moveCenter(deltaVelocity)))) {
				colRe.setWillNotIntersect();
			}
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
					if (distIs > 0) {
						colRe.setNotIntersecting();
					}
					if (distWill > 0) {
						colRe.setWillNotIntersect();
					}
					if (!(colRe.isIntersecting() && colRe.willIntersect())) {
						return colRe;
					} else {
						distIs = -distIs;
						distWill = -distWill;
						if (distIs < minOverlapDistanceIs) {
							minOverlapDistanceIs = distIs;
							if (this.getCenter().sub(other.getCenter())
									.mul(curAxis) < 0) {
								translationAxisIs = curAxis.neg();
							} else {
								translationAxisIs = curAxis;
							}
						}
						if (distWill < minOverlapDistanceWill) {
							minOverlapDistanceWill = distWill;
							if (this.getCenter().sub(other.getCenter())
									.mul(curAxis) < 0) {
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
		return this.moveCenter(newCenter.sub(this.center));
	}

	@Override
	public ConvexShape moveCenter(Vector diff) {
		return new NextPolygon(this.center.add(diff), this.relativePoints,
				this.axises, this.left + diff.x, this.right + diff.x, this.up
						+ diff.y, this.down + diff.y, this.getBoundingRect()
						.moveCenter(diff));
	}

	@Override
	public Vector getCenter() {
		return this.center;
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

	public boolean isFinished() {
		return this.finished;
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

	@Override
	public Shape toSlickShape() {
		Polygon poly = new Polygon();
		for (Vector relPoint : this.getPoints()) {
			poly.addPoint(relPoint.x, relPoint.y);
		}
		poly.setClosed(true);
		return poly;
	}

	@Override
	public NextShape scale(float zoom) {
		ArrayList<Vector> newPoints = new ArrayList<Vector>();
		for (Vector vector : this.relativePoints) {
			newPoints.add(vector.mul(zoom));
		}
		NextPolygon poly = new NextPolygon(this.center, newPoints);
		poly.finish();
		return poly;
	}

	@Override
	public boolean isPointIn(Vector p) {
		// Angewendet wird die allgemeine Strahlen-Methode
		// (siehe http://rw7.de/ralf/inffaq/polygon.html#strahl)
		p = p.sub(this.center);
		Ray testRay = new Ray(p, Vector.RIGHT);
		int n = 0;
		for (PointLine line : this.getRelativeLines()) {
			if (testRay.crosses(line)) {
				n++;
			}
		}
		return n % 2 == 1;
	}

	@Override
	public String toString() {
		return "Polygon --> Center: " + this.center + " RelativePoints: "
				+ this.relativePoints;
	}

	@Override
	public HelpRectangle getBoundingRect() {
		if (this.boundingRect == null) {
			this.buildBoundingRect();
		}
		return this.boundingRect;
	}

	@Override
	public NextShape rotate(float ang) {
		ArrayList<Vector> points = new ArrayList<Vector>();
		for (Vector vec : this.relativePoints) {
			points.add(vec.rotate(ang));
		}
		NextPolygon re = new NextPolygon(this.center, points);
		re.finish();
		return re;
	}
}
