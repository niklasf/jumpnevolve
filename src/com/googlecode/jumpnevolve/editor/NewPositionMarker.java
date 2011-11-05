package com.googlecode.jumpnevolve.editor;

import javax.swing.text.AbstractDocument.Content;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.gui.ContentListener;
import com.googlecode.jumpnevolve.graphics.gui.Contentable;
import com.googlecode.jumpnevolve.graphics.gui.DialogPart;
import com.googlecode.jumpnevolve.graphics.gui.Informable;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceVectorTextfield;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * @author Erik Wagner
 * 
 */
public class NewPositionMarker extends NewEditorArgument implements
		ContentListener {

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
	private NewPositionMarker parent;

	protected final DialogPart dialogPart;

	/**
	 * @param editor
	 */
	public NewPositionMarker(Editor2 editor, String name, int modus,
			Vector startPosition, Color color) {
		super(editor);
		this.modus = modus;
		this.position = startPosition;
		this.color = color;
		this.dialogPart = new DialogPart(new InterfaceVectorTextfield(), name);
		this.dialogPart.part.addContentListener(this);
		this.createShape();
	}

	private void createShape() {
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

	public Vector getPosition() {
		return this.position;
	}

	public boolean isMoving() {
		return this.wasInCircle;
	}

	protected void changePosition(Vector newPosition) {
		this.position = newPosition;
		this.shape = this.shape.modifyCenter(this.getPosition());
		this.updateDialogPart();
	}

	protected void updateDialogPart() {
		this.dialogPart.part.setContent("" + this.position);
	}

	protected void updateFromDialogPart(String newContent) {
		// TODO Auto-generated method stub

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

	@Override
	public void initialize(String value) {
		switch (this.modus) {
		case MODUS_BOTH:
			this.changePosition(Vector.parseVector(value));
			break;
		case MODUS_X:
			this.changePosition(Vector.parseVector(value + "|"
					+ this.getParentPosition().y));
			break;
		case MODUS_Y:
			this.changePosition(Vector.parseVector(this.getParentPosition().x
					+ "|" + value));
			break;
		default:
			System.out.println("UNKOWN-MODUS: " + this.modus);
			this.changePosition(Vector.parseVector(value));
			break;
		}
	}

	public Vector getParentPosition() {
		if (this.parent != null) {
			return this.parent.getPosition();
		} else {
			return Vector.ZERO;
		}
	}

	public void setParent(NewPositionMarker parent) {
		this.parent = parent;
	}

	public NewPositionMarker getParent() {
		return this.parent;
	}

	@Override
	public NewEditorArgument getClone(Editor2 editor,
			NewEditorArgument parentArgs[]) {
		NewPositionMarker re = new NewPositionMarker(editor,
				this.getDialogPart().name, this.modus, this.position,
				this.color);
		if (parentArgs.length > 0) {
			if (parentArgs[0] instanceof NewPositionMarker) {
				re.setParent((NewPositionMarker) parentArgs[0]);
			}
		}
		return re;
	}

	@Override
	public DialogPart getDialogPart() {
		return this.dialogPart;
	}

	@Override
	public void contentChanged(Contentable object) {
		this.updateFromDialogPart(object.getContent());
	}

}
