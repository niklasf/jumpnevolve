package com.googlecode.jumpnevolve.editor.arguments;

import com.googlecode.jumpnevolve.editor.Editor;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.gui.DialogPart;

/**
 * @author Erik Wagner
 * 
 *         TODO: Andere Subklassen als PositionMarker erstellen
 */
public abstract class EditorArgument implements Pollable, Drawable {

	private final Editor editor;

	public abstract String getArgumentPart();

	public abstract void initialize(String value);

	public abstract EditorArgument getClone(Editor editor);

	public abstract void setArguments(EditorArgument parentArgs[]);

	public abstract DialogPart getDialogPart();

	public EditorArgument(Editor editor) {
		this.editor = editor;
	}

	@Override
	public String toString() {
		// Paketnamen abtrennen
		String superString = super.toString();
		return superString.substring(superString.lastIndexOf('.') + 1);
	}

	/**
	 * @return Der Editor, dem das EditorArgument zugeordnet ist
	 */
	public Editor getEditor() {
		return this.editor;
	}
}
