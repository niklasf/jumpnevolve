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
 * @author e.wagner
 * 
 *         FIXME: Noch fehlerhaft
 * 
 */
public class RelativePositionMarker extends PositionMarker {

	private Vector lastParentPosition = Vector.ZERO;

	public RelativePositionMarker(int modus, Vector startPosition, Color color) {
		super(modus, startPosition, color);
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
				this.position, this.color);
		re.setParent(this.parent);
		return re;
	}
}
