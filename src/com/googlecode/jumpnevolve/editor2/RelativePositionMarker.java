package com.googlecode.jumpnevolve.editor2;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Positionsmarker, der eine Koordinate angibt, die geändert wird, wenn sich
 * die Position des "Parents" ändert
 * 
 * Reagiert nicht, während sich die Position des "Parents" ändert
 * 
 * TODO: Benötigt in jedem Fall ein EditorObject als parent
 * 
 * @author e.wagner
 * 
 *         FIXME: Noch fehlerhaft
 * 
 */
public class RelativePositionMarker extends PositionMarker {

	public static final int OUTPUT_MODUS_RELATIVE = 0;
	public static final int OUTPUT_MODUS_ABSOLUT = 1;

	private Vector lastParentPosition = Vector.ZERO;

	private final int outputModus;

	public RelativePositionMarker(int modus, int outputModus,
			Vector startPosition, Color color) {
		super(modus, startPosition, color);
		this.outputModus = outputModus;
	}

	private void addToPosition(Vector toAdd) {
		this.changePosition(this.getPosition().add(toAdd));
	}

	@Override
	public void poll(Input input, float secounds) {
		Vector parentPosition = this.parent.getPosition();
		Vector diff = parentPosition.sub(this.lastParentPosition);
		this.addToPosition(diff);
		if (!diff.equals(Vector.ZERO)) {
			this.wasInCircle = false;
		}
		super.poll(input, secounds);
		this.lastParentPosition = parentPosition;
	}

	@Override
	public EditorArgument clone() {
		EditorArgument re = new RelativePositionMarker(this.modus,
				this.outputModus, this.position, this.color);
		re.setParent(this.parent);
		re.setEditor(this.editor);
		return re;
	}

	@Override
	public String getArgumentPart() {
		switch (this.outputModus) {
		case OUTPUT_MODUS_RELATIVE:
			switch (this.modus) {
			case MODUS_BOTH:
				return this.position.sub(this.parent.getPosition()).toString();
			case MODUS_X:
				return "" + this.position.sub(this.parent.getPosition()).x;
			case MODUS_Y:
				return "" + this.position.sub(this.parent.getPosition()).y;
			default:
				return this.position.sub(this.parent.getPosition()).toString();
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
					+ this.parent.getPosition().y));
			break;
		case MODUS_Y:
			this.changePosition(Vector.parseVector(this.parent.getPosition().x
					+ "|" + value));
			break;
		case MODUS_BOTH:
		default:
			this.changePosition(Vector.parseVector(value));
			break;
		}
		if (this.outputModus == OUTPUT_MODUS_RELATIVE) {
			this.addToPosition(this.parent.getPosition());
		}
		this.lastParentPosition = this.parent.getPosition();
	}
}
