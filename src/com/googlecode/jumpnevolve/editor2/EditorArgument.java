package com.googlecode.jumpnevolve.editor2;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;

/**
 * @author e.wagner
 * 
 */
public abstract class EditorArgument implements Pollable, Drawable {

	public final EditorObject parent;

	public abstract String getArgumentPart();

	public abstract EditorArgument clone();

	public EditorArgument(EditorObject parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		// Paketnamen abtrennen
		String superString = super.toString();
		return superString.substring(superString.lastIndexOf('.') + 1);
	}
}
