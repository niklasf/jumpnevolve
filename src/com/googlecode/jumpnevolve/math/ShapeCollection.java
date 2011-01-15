/**
 * 
 */
package com.googlecode.jumpnevolve.math;

import org.newdawn.slick.geom.MorphShape;

/**
 * Eine ShapeCollection besteht aus einer Reihe sich überlagernder Shapes
 * 
 * @author Erik Wagner
 * 
 */
public class ShapeCollection implements Shape {

	private final Shape[] shapes;
	private final Rectangle boundingRectangle;

	/**
	 * 
	 */
	public ShapeCollection(Shape[] shapes) {
		this.shapes = shapes;
		float up = shapes[0].getUpperEnd(), down = shapes[0].getLowerEnd(), left = shapes[0]
				.getLeftEnd(), right = shapes[0].getRightEnd();
		for (Shape shape : shapes) {
			up = Math.min(up, shape.getUpperEnd());
			down = Math.max(up, shape.getLowerEnd());
			right = Math.max(up, shape.getRightEnd());
			left = Math.min(up, shape.getLeftEnd());
		}
		this.boundingRectangle = new Rectangle(new Vector((left + right) / 2,
				(up + down) / 2), right - left, down - up);
	}

	private ShapeCollection(Shape[] shapes, Rectangle boundingRectangle) {
		this.shapes = shapes;
		this.boundingRectangle = boundingRectangle;
	}

	@Override
	public boolean doesCollide(Shape other) {
		for (Shape shape : this.shapes) {
			if (shape.doesCollide(other)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Circle getBestCircle() {
		return this.boundingRectangle.getBestCircle();
	}

	@Override
	public Vector getCenter() {
		return this.boundingRectangle.getCenter();
	}

	@Override
	public Collision getCollision(Shape other, boolean otherMoveable,
			boolean thisMoveable) {
		Collision col = new Collision();
		for (Shape shape : this.shapes) {
			if (shape.doesCollide(other)) {
				col.addCollision(shape.getCollision(other, otherMoveable,
						thisMoveable));
			}
		}
		return col;
	}

	@Override
	public float getDistanceToSide(byte direction) {
		return this.boundingRectangle.getDistanceToSide(direction);
	}

	@Override
	public float getLeftEnd() {
		return this.boundingRectangle.getLeftEnd();
	}

	@Override
	public float getLowerEnd() {
		return this.boundingRectangle.getLowerEnd();
	}

	@Override
	public float getRightEnd() {
		return this.boundingRectangle.getRightEnd();
	}

	@Override
	public float getUpperEnd() {
		return this.boundingRectangle.getUpperEnd();
	}

	@Override
	public float getXRange() {
		return this.boundingRectangle.getXRange();
	}

	@Override
	public float getYRange() {
		return this.boundingRectangle.getYRange();
	}

	@Override
	public boolean isPointInThis(Vector p) {
		for (Shape shape : this.shapes) {
			if (shape.isPointInThis(p)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Shape modifyCenter(Vector center) {
		Vector diff = center.sub(this.boundingRectangle.getCenter());
		Shape[] newShapes = new Shape[this.shapes.length];
		for (int i = 0; i < newShapes.length; i++) {
			newShapes[i] = this.shapes[i].modifyCenter(this.shapes[i]
					.getCenter().add(diff));
		}
		return new ShapeCollection(newShapes,
				(Rectangle) this.boundingRectangle.modifyCenter(center));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.jumpnevolve.math.Shape#toSlickShape()
	 */
	@Override
	public org.newdawn.slick.geom.Shape toSlickShape() {
		// FIXME: MorphShape ist glaube ich nicht das richtige entsprechende
		// Shape --> vllt. besser boundingRectangle.toSlickShape() zurückgeben?
		MorphShape re = new MorphShape(shapes[0].toSlickShape());
		for (int i = 1; i < shapes.length; i++) {
			re.addShape(shapes[i].toSlickShape());
		}
		return re;
	}

}
