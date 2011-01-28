package com.googlecode.jumpnevolve.editor2;

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

	private int width, height;
	private NextShape normalRect, littleRect, bigRect, horizontalRect,
			verticalRect;

	public RectangleDimension(EditorObject parent, int startWidth,
			int startHeight) {
		super(parent);
		this.width = startWidth;
		this.height = startHeight;
		this.changeDimensions(startWidth, startHeight);
	}

	private void changeDimensions(int width, int height) {
		float distWidth = WANTED_DIST, distHeight = WANTED_DIST;
		while (width - distWidth < 1) {
			distWidth = distWidth / 2;
		}
		while (height - distHeight < 1) {
			distHeight = distHeight / 2;
		}
		this.normalRect = ShapeFactory.createRectangle(this.parent
				.getPosition(), width, height);
		this.littleRect = ShapeFactory.createRectangle(this.parent
				.getPosition(), width - distWidth, height - distHeight);
		this.bigRect = ShapeFactory.createRectangle(this.parent.getPosition(),
				width + distWidth, height + distHeight);
		this.horizontalRect = ShapeFactory.createRectangle(this.parent
				.getPosition(), width + distWidth, height - distHeight);
		this.verticalRect = ShapeFactory.createRectangle(this.parent
				.getPosition(), width - distWidth, height + distHeight);
	}

	@Override
	public String getArgumentPart() {
		return width / 2 + "|" + height / 2;
	}

	@Override
	public void poll(Input input, float secounds) {
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			Vector mousePos = this.parent.parent.translateMousePos(input
					.getMouseX(), input.getMouseY());
			if (!this.littleRect.isPointIn(mousePos)
					&& this.bigRect.isPointIn(mousePos)) {
				if (this.horizontalRect.isPointIn(mousePos)) {
					this.width = (int) Math.abs(this.parent.getPosition().x
							- mousePos.x);
				} else if (this.verticalRect.isPointIn(mousePos)) {
					this.height = (int) Math.abs(this.parent.getPosition().y
							- mousePos.y);
				} else {
					this.width = (int) Math.abs(this.parent.getPosition().x
							- mousePos.x);
					this.height = (int) Math.abs(this.parent.getPosition().y
							- mousePos.y);
				}
				this.changeDimensions(width, height);
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.draw(g, this.normalRect);
	}

	@Override
	public EditorArgument clone() {
		return new RectangleDimension(this.parent, this.width, this.height);
	}

}
