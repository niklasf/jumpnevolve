package com.googlecode.jumpnevolve.editor;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.math.Vector;

public class NewRectangleDimension extends NewRelativePositionMarker {

	private NewRelativePositionMarker referenceDirection;
	private Vector lastRefernceDirection = Vector.UP;

	public NewRectangleDimension(Editor2 editor,
			NewPositionMarker referencePoint, String name,
			Vector startPosition, Color color) {
		super(editor, referencePoint, name, MODUS_BOTH, OUTPUT_MODUS_RELATIVE,
				startPosition, color);
	}

	public NewRectangleDimension(Editor2 editor,
			NewPositionMarker referencePoint,
			NewRelativePositionMarker referenceDirection, String name,
			Vector startPosition, Color color) {
		super(editor, referencePoint, name, MODUS_BOTH, OUTPUT_MODUS_RELATIVE,
				startPosition, color);
		this.referenceDirection = referenceDirection;
		this.lastRefernceDirection = this.getReferenceVector();
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		Vector newDirection = this.getReferenceVector();
		float ang = newDirection.clockWiseAng()
				- this.lastRefernceDirection.clockWiseAng();
		this.calculateNewPosition(ang);
		this.lastRefernceDirection = newDirection;
	}

	private void calculateNewPosition(float ang) {
		this.position = this.position.rotate(ang, this.getParentPosition());
	}

	@Override
	public NewEditorArgument getClone(Editor2 editor) {
		return new NewRectangleDimension(editor, this.getParent(),
				this.referenceDirection, this.getDialogPart().name,
				this.position, color);
	}

	@Override
	public void initialize(String value) {
		Vector dimension = Vector.parseVector(value);
		this.position = dimension.rotate(
				this.getReferenceVector().clockWiseAng()).add(
				this.getParentPosition());
	}

	@Override
	public void setArguments(NewEditorArgument[] parentArgs) {
		if (parentArgs.length > 1) {
			if (parentArgs[0] instanceof NewPositionMarker
					&& parentArgs[1] instanceof NewRelativePositionMarker) {
				this.setParent((NewPositionMarker) parentArgs[0]);
				this.setReferenceDirection((NewRelativePositionMarker) parentArgs[1]);
			}
			if (parentArgs[1] instanceof NewPositionMarker
					&& parentArgs[0] instanceof NewRelativePositionMarker) {
				this.setParent((NewPositionMarker) parentArgs[1]);
				this.setReferenceDirection((NewRelativePositionMarker) parentArgs[0]);
			}
		}
		if (parentArgs.length > 0) {
			if (parentArgs[0] instanceof NewPositionMarker) {
				this.setParent((NewPositionMarker) parentArgs[0]);
			}
			if (parentArgs[0] instanceof NewRelativePositionMarker) {
				this.setReferenceDirection((NewRelativePositionMarker) parentArgs[0]);
			}
		}
	}

	private void setReferenceDirection(
			NewRelativePositionMarker referenceDirection) {
		this.referenceDirection = referenceDirection;
		this.lastRefernceDirection = this.getReferenceVector();
	}

	@Override
	public String getArgumentPart() {
		// pos: aktuelle Position dieses Markers
		// ref: Richtung nach der das Rechteck ausgerichtet wird
		Vector pos = this.getPosition().sub(this.getParentPosition()), ref = this
				.getReferenceVector(), re = Vector.ZERO;

		// Vektor entsprechend der Ausrichtung berechnen
		re = new Vector(pos.mul(ref), pos.mul(ref
				.rotate((float) (Math.PI / 2.0f))));

		re = pos.rotate(-ref.clockWiseAng());
		// Jedes Rechteck muss über Breite und Höhe verfügen
		if ((int) re.x == 0) {
			re = re.modifyX(1);
		}
		if ((int) re.y == 0) {
			re = re.modifyY(1);
		}
		return re.toString();
	}

	private Vector getReferenceVector() {
		if (this.referenceDirection != null) {
			return this.referenceDirection.getRelativePosition().getDirection();
		} else {
			return Vector.UP;
		}
	}

	@Override
	public String toString() {
		// Paketnamen abtrennen
		String superString = super.toString();
		return superString.substring(superString.lastIndexOf('.') + 1);
	}
}
