package com.googlecode.jumpnevolve.editor2;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author e.wagner
 * 
 */
public class RectangleDimension extends EditorArgument {

	private static final float WANTED_DIST = 5.0f;
	private static final float MIN_DIST = 2.0f;

	private int width, height;
	private NextShape markCircle1, markCircle2, markCircle3, markCircle4;
	private Vector lastPosition = Vector.ZERO;

	private boolean wasChanged;

	public RectangleDimension(int startWidth, int startHeight) {
		super();
		this.width = startWidth;
		this.height = startHeight;
		this.updateCircles();
	}

	private void changeDimensions(Vector translatedMousePos) {
		this.width = (int) Math.max(
				Math.abs(this.getPosition().x - translatedMousePos.x) * 2, 2);
		this.height = (int) Math
				.max(Math.abs(this.parent.getPosition().y
						- translatedMousePos.y) * 2, 2);
	}

	private void updateCircles() {
		float dist = WANTED_DIST;
		this.markCircle1 = ShapeFactory.createCircle(
				this.getPosition().add(this.width / 2, this.height / 2), dist);
		this.markCircle2 = ShapeFactory.createCircle(
				this.getPosition().add(-this.width / 2, this.height / 2), dist);
		this.markCircle3 = ShapeFactory
				.createCircle(
						this.getPosition().add(-this.width / 2,
								-this.height / 2), dist);
		this.markCircle4 = ShapeFactory.createCircle(
				this.getPosition().add(this.width / 2, -this.height / 2), dist);
	}

	private Vector getPosition() {
		if (this.parent != null) {
			return this.parent.getPosition();
		} else {
			return Vector.ZERO;
		}
	}

	@Override
	public String getArgumentPart() {
		return width / 2 + "|" + height / 2;
	}

	@Override
	public void poll(Input input, float secounds) {
		if (!this.getPosition().equals(this.lastPosition)) {
			this.updateCircles();
		} else {
			Vector translatedMousePos = this.parent.parent.translateMousePos(
					input.getMouseX(), input.getMouseY());
			if (this.wasChanged) {
				this.changeDimensions(translatedMousePos);
				this.updateCircles();
			}
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
					&& !this.parent.isMoving()) {
				if (this.markCircle1.isPointIn(translatedMousePos)
						|| this.markCircle2.isPointIn(translatedMousePos)
						|| this.markCircle3.isPointIn(translatedMousePos)
						|| this.markCircle4.isPointIn(translatedMousePos)) {
					this.wasChanged = true;
				} else {
					this.wasChanged = false;
				}
			} else {
				this.wasChanged = false;
			}
		}
		// }
		this.lastPosition = this.getPosition();
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.draw(g, this.markCircle1, Color.blue);
		GraphicUtils.draw(g, this.markCircle2, Color.blue);
		GraphicUtils.draw(g, this.markCircle3, Color.blue);
		GraphicUtils.draw(g, this.markCircle4, Color.blue);
	}

	@Override
	public EditorArgument clone() {
		EditorArgument re = new RectangleDimension(this.width, this.height);
		re.setParent(this.parent);
		return re;
	}

	@Override
	public void initialize(String value) {
		this.changeDimensions(this.getPosition().add(Vector.parseVector(value)));
	}

}
