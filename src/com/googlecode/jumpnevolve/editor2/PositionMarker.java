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
public class PositionMarker extends EditorArgument {

	public static final int MODUS_BOTH = 0;
	public static final int MODUS_X = 1;
	public static final int MODUS_Y = 2;

	private static final float radius = 3.0f;

	protected final int modus;
	protected final Color color;
	protected Vector position;
	private NextShape circle;
	private boolean wasInCircle = false;

	/**
	 * @param parent
	 */
	public PositionMarker(int modus, Vector startPosition, Color color) {
		super();
		this.modus = modus;
		this.position = startPosition;
		this.color = color;
		this.circle = ShapeFactory.createCircle(this.getPosition(), radius);
	}

	@Override
	public String getArgumentPart() {
		switch (this.modus) {
		case MODUS_BOTH:
			return this.position.toString();
		case MODUS_X:
			return "" + this.position.x;
		case MODUS_Y:
			return "" + this.position.y;
		default:
			return this.position.toString();
		}
	}

	public Vector getPosition() {
		System.out.println("P");
		return this.position;
	}

	public boolean isMoving() {
		return this.wasInCircle;
	}

	private void changePosition(Vector newPosition) {
		this.position = newPosition;
		this.circle = this.circle.modifyCenter(this.getPosition());
	}

	@Override
	public void poll(Input input, float secounds) {
		Vector mousePos = this.parent.parent.translateMousePos(
				input.getMouseX(), input.getMouseY());
		if (this.wasInCircle) {
			this.changePosition(mousePos);
		}
		if (this.circle.isPointIn(mousePos)
				&& input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			this.wasInCircle = true;
		} else {
			this.wasInCircle = false;
		}
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.draw(g, this.circle, this.color);
	}

	@Override
	public EditorArgument clone() {
		EditorArgument re = new PositionMarker(this.modus, this.position,
				this.color);
		re.setParent(this.parent);
		return re;
	}

}
