package com.googlecode.jumpnevolve.editor;

import org.newdawn.slick.Color;

import com.googlecode.jumpnevolve.math.Vector;

public class NewRectangleDimension extends NewRelativePositionMarker {

	private NewRelativePositionMarker referenceDirection;

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
	}

	@Override
	public NewEditorArgument getClone(Editor2 editor,
			NewEditorArgument parentArgs[]) {
		if (parentArgs.length > 1) {
			if (parentArgs[0] instanceof NewPositionMarker
					&& parentArgs[1] instanceof NewRelativePositionMarker) {
				return new NewRectangleDimension(editor,
						(NewPositionMarker) parentArgs[0],
						(NewRelativePositionMarker) parentArgs[1],
						this.getDialogPart().name, this.position, this.color);
			}
			if (parentArgs[1] instanceof NewPositionMarker
					&& parentArgs[0] instanceof NewRelativePositionMarker) {
				return new NewRectangleDimension(editor,
						(NewPositionMarker) parentArgs[1],
						(NewRelativePositionMarker) parentArgs[0],
						this.getDialogPart().name, this.position, this.color);
			}
		}
		if (parentArgs.length > 0) {
			if (parentArgs[0] instanceof NewPositionMarker) {
				return new NewRectangleDimension(editor,
						(NewPositionMarker) parentArgs[0],
						this.referenceDirection, this.getDialogPart().name,
						this.position, this.color);
			}
			if (parentArgs[0] instanceof NewRelativePositionMarker) {
				return new NewRectangleDimension(editor, this.getParent(),
						(NewRelativePositionMarker) parentArgs[0],
						this.getDialogPart().name, this.position, this.color);
			}
		}
		return new NewRectangleDimension(editor, this.getParent(),
				this.referenceDirection, this.getDialogPart().name,
				this.position, color);
	}

	@Override
	public String getArgumentPart() {
		// pos: aktuelle Position dieses Markers
		// ref: Richtung nach der das Rechteck ausgerichtet wird
		Vector pos = this.getPosition(), ref = this.getReferenceVector(), re;

		// Vektor entsprechend der Ausrichtung berechnen
		re = new Vector(pos.mul(ref), pos.mul(ref
				.rotate((float) (Math.PI / 2.0f))));

		// Jedes Rechteck muss über Breite und Höhe verfügen
		if (re.x == 0) {
			re = re.modifyX(1);
		}
		if (re.y == 0) {
			re = re.modifyY(1);
		}
		return re.toString();
	}

	private Vector getReferenceVector() {
		if (this.referenceDirection != null) {
			return this.referenceDirection.getRelativePosition();
		} else {
			return Vector.UP;
		}
	}
}
