package com.googlecode.jumpnevolve.editor2;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;

/**
 * @author e.wagner
 * 
 */
public abstract class EditorArgument implements Pollable, Drawable {

	protected EditorObject parent;

	protected Editor2 editor;

	public abstract String getArgumentPart();

	public abstract void initialize(String value);

	public abstract EditorArgument clone();

	public EditorArgument() {
	}

	public EditorObject getParent() {
		return this.parent;
	}

	public void setParent(EditorObject parent) {
		this.parent = parent;
	}

	public void setEditor(Editor2 editor) {
		this.editor = editor;
	}

	public Editor2 getEditor() {
		if (this.editor != null) {
			return this.editor;
		} else {
			if (this.parent != null) {
				return this.parent.parent;
			} else {
				return null;
			}
		}
	}

	@Override
	public String toString() {
		// Paketnamen abtrennen
		String superString = super.toString();
		return superString.substring(superString.lastIndexOf('.') + 1);
	}
}
