package com.googlecode.jumpnevolve.editor.arguments;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.editor.Editor;
import com.googlecode.jumpnevolve.math.Vector;

public class RectangleDimension extends RelativePositionMarker {

	private RelativePositionMarker referenceDirection;
	private Vector lastRefernceDirection = Vector.UP;

	public RectangleDimension(Editor editor, PositionMarker referencePoint,
			String name, Vector startPosition, Color color) {
		super(editor, referencePoint, name, MODUS_BOTH, OUTPUT_MODUS_RELATIVE,
				startPosition, color);
	}

	public RectangleDimension(Editor editor, PositionMarker referencePoint,
			RelativePositionMarker referenceDirection, String name,
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
	public EditorArgument getClone(Editor editor) {
		return new RectangleDimension(editor, this.getParent(),
				this.referenceDirection, this.getDialogPart().name,
				this.position, color);
	}

	@Override
	public void initialize(String value) {
		Vector dimension = Vector.parseVector(value).rotate(
				this.getReferenceVector().clockWiseAng());
		this.lastRefernceDirection = this.getReferenceVector();
		super.initialize(dimension.toString());
	}

	@Override
	public void setArguments(EditorArgument[] parentArgs) {
		if (parentArgs.length > 1) {
			if (parentArgs[0] instanceof PositionMarker
					&& parentArgs[1] instanceof RelativePositionMarker) {
				this.setParent((PositionMarker) parentArgs[0]);
				this.setReferenceDirection((RelativePositionMarker) parentArgs[1]);
			}
			if (parentArgs[1] instanceof PositionMarker
					&& parentArgs[0] instanceof RelativePositionMarker) {
				this.setParent((PositionMarker) parentArgs[1]);
				this.setReferenceDirection((RelativePositionMarker) parentArgs[0]);
			}
		}
		if (parentArgs.length > 0) {
			if (parentArgs[0] instanceof PositionMarker) {
				this.setParent((PositionMarker) parentArgs[0]);
			}
			if (parentArgs[0] instanceof RelativePositionMarker) {
				this.setReferenceDirection((RelativePositionMarker) parentArgs[0]);
			}
		}
	}

	private void setReferenceDirection(RelativePositionMarker referenceDirection) {
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
		re = pos.rotate(-ref.clockWiseAng());

		// Dimensionsangaben sind immer positiv
		// Jedes Rechteck muss über Breite und Höhe verfügen
		re = re.modifyX(Math.max(Math.abs(re.x), 1));
		re = re.modifyY(Math.max(Math.abs(re.y), 1));

		// Vektor als String zurückgeben
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
