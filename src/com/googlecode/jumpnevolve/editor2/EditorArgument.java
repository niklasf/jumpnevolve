package com.googlecode.jumpnevolve.editor2;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;

/**
 * @author e.wagner
 * 
 */
public abstract class EditorArgument implements Pollable, Drawable {

	protected EditorObject parent;

	public abstract String getArgumentPart();

	public abstract EditorArgument clone();

	public EditorArgument() {
	}

	public EditorObject getParent() {
		return this.parent;
	}

	public void setParent(EditorObject parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		// Paketnamen abtrennen
		String superString = super.toString();
		return superString.substring(superString.lastIndexOf('.') + 1);
	}
}
