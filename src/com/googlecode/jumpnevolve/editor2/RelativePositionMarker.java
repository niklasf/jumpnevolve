package com.googlecode.jumpnevolve.editor2;

import org.newdawn.slick.Color;

import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Positionsmarker, der eine relative Koordinate angibt
 * 
 * {@link getPosition} Gibt nicht die relative Koordinate, sondern die
 * Koordinate für die Zeichenfläche zurück
 * 
 * {@link getRelativePosition} Gibt die relative Koordinate zurück
 * 
 * {@link getArgumentPart} Gibt die realtive Koordinate zurück
 * 
 * @author e.wagner
 * 
 *         FIXME: Noch fehlerhaft
 * 
 */
public class RelativePositionMarker extends PositionMarker {

	public RelativePositionMarker(int modus, Vector startPosition, Color color) {
		super(modus, startPosition, color);
	}

	@Override
	public Vector getPosition() {
		if (this.getParent() != null) {
			System.out.println("Position: "
					+ this.getParent().getPosition().add(super.getPosition()));
			return this.getParent().getPosition().add(super.getPosition());
		} else {
			System.out.println("No Parent");
			return super.getPosition();
		}
	}

	public Vector getRelativePosition() {
		return super.getPosition();
	}

	@Override
	public EditorArgument clone() {
		EditorArgument re = new RelativePositionMarker(this.modus,
				this.position, this.color);
		re.setParent(this.parent);
		return re;
	}
}
