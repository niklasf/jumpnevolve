package com.googlecode.jumpnevolve.editor.arguments;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.editor.Editor2;
import com.googlecode.jumpnevolve.math.Vector;

public class RelativePositionMarker extends PositionMarker {

	public static final int OUTPUT_MODUS_RELATIVE = 0;
	public static final int OUTPUT_MODUS_ABSOLUT = 1;

	private Vector lastParentPosition = Vector.ZERO;

	protected final int outputModus;

	public RelativePositionMarker(Editor2 editor,
			PositionMarker referencePoint, String name, int modus,
			int outputModus, Vector startPosition, Color color) {
		super(editor, name, modus, startPosition, color);
		this.outputModus = outputModus;
		this.setParent(referencePoint);
	}

	public Vector getRelativePosition() {
		return this.position.sub(this.getParentPosition());
	}

	private void addToPosition(Vector toAdd) {
		this.changePosition(this.getPosition().add(toAdd));
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		Vector parentPosition = this.getParentPosition();
		Vector diff = parentPosition.sub(this.lastParentPosition);
		this.addToPosition(diff);
		this.lastParentPosition = parentPosition;
	}

	@Override
	public EditorArgument getClone(Editor2 editor) {
		return new RelativePositionMarker(editor, this.getParent(),
				this.getDialogPart().name, this.modus, this.outputModus,
				this.position, this.color);
	}

	@Override
	public String getArgumentPart() {
		switch (this.outputModus) {
		case OUTPUT_MODUS_RELATIVE:
			switch (this.modus) {
			case MODUS_BOTH:
				return this.position.sub(this.getParentPosition()).toString();
			case MODUS_X:
				return "" + this.position.sub(this.getParentPosition()).x;
			case MODUS_Y:
				return "" + this.position.sub(this.getParentPosition()).y;
			default:
				return this.position.sub(this.getParentPosition()).toString();
			}
		case OUTPUT_MODUS_ABSOLUT:
		default:
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

	}

	@Override
	public void initialize(String value) {
		switch (this.modus) {
		case MODUS_X:
			this.changePosition(Vector.parseVector(value + "|"
					+ this.getParentPosition().y));
			break;
		case MODUS_Y:
			this.changePosition(Vector.parseVector(this.getParentPosition().x
					+ "|" + value));
			break;
		case MODUS_BOTH:
		default:
			this.changePosition(Vector.parseVector(value));
			break;
		}
		if (this.outputModus == OUTPUT_MODUS_RELATIVE) {
			this.addToPosition(this.getParentPosition());
		}
		this.lastParentPosition = this.getParentPosition();
	}

	@Override
	public String toString() {
		// Paketnamen abtrennen
		String superString = super.toString();
		return superString.substring(superString.lastIndexOf('.') + 1);
	}
}
