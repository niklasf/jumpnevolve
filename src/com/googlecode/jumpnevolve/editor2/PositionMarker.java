package com.googlecode.jumpnevolve.editor2;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.PointLine;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * @author e.wagner
 * 
 */
public class PositionMarker extends EditorArgument {

	/**
	 * Konstante für eine Positionsmarkierung mit beiden Koordinaten
	 */
	public static final int MODUS_BOTH = 0;

	/**
	 * Konstante für eine Positionsmarkierung nur mit der X-Koordinate
	 */
	public static final int MODUS_X = 1;

	/**
	 * Konstante für eine Positionsmarkierung nur mit der Y-Koordinate
	 */
	public static final int MODUS_Y = 2;

	/**
	 * Die Größendimension der Markierungen
	 */
	private static final float DIMENSION = Parameter.EDITOR_POSITIONMARKER_DIMENSION;

	protected final int modus;
	protected final Color color;
	protected Vector position;
	private NextShape shape;
	protected boolean wasInCircle = false;

	/**
	 * Erstellt einen Positionsmarker, der eine Stelle im Editor markiert
	 * 
	 * @param modus
	 *            Die Art des Positionsmarkers {@link #MODUS_BOTH} ,
	 *            {@link #MODUS_X}, {@link #MODUS_Y}
	 * @param startPosition
	 *            Die Startpositions des Markers
	 * @param color
	 *            Die Farbe, mit der der Marker gezeichnet werden soll
	 */
	public PositionMarker(int modus, Vector startPosition, Color color) {
		super();
		this.modus = modus;
		this.position = startPosition;
		this.color = color;
		switch (this.modus) {
		case MODUS_BOTH:
			this.shape = ShapeFactory.createCircle(this.getPosition(),
					DIMENSION);
			break;
		case MODUS_X:
			this.shape = ShapeFactory.createRectangle(this.getPosition(),
					DIMENSION, DIMENSION * 3);
			break;
		case MODUS_Y:
			this.shape = ShapeFactory.createRectangle(this.getPosition(),
					DIMENSION * 3, DIMENSION);
			break;
		default:
			this.shape = ShapeFactory.createCircle(this.getPosition(),
					DIMENSION);
			break;
		}
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
		return this.position;
	}

	public boolean isMoving() {
		return this.wasInCircle;
	}

	protected void changePosition(Vector newPosition) {
		this.position = newPosition;
		this.shape = this.shape.modifyCenter(this.getPosition());
	}

	@Override
	public void poll(Input input, float secounds) {
		Vector mousePos = this.getEditor().translateMousePos(input.getMouseX(),
				input.getMouseY());
		if (this.wasInCircle) {
			this.changePosition(mousePos);
		}
		if (this.shape.isPointIn(mousePos)
				&& input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			this.wasInCircle = true;
		} else {
			this.wasInCircle = false;
		}
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.draw(g, this.shape, this.color);
	}

	@Override
	public EditorArgument clone() {
		EditorArgument re = new PositionMarker(this.modus, this.position,
				this.color);
		re.setParent(this.parent);
		re.setEditor(this.editor);
		return re;
	}

	@Override
	public void initialize(String value) {
		switch (this.modus) {
		case MODUS_BOTH:
			this.changePosition(Vector.parseVector(value));
			break;
		case MODUS_X:
			this.changePosition(Vector.parseVector(value + "|"
					+ this.parent.getPosition().y));
			break;
		case MODUS_Y:
			System.out.println(this.parent.getPosition().x + "|" + value);
			this.changePosition(Vector.parseVector(this.parent.getPosition().x
					+ "|" + value));
			break;
		default:
			System.out.println("MODUS: " + this.modus);
			this.changePosition(Vector.parseVector(value));
			break;
		}
	}
}
