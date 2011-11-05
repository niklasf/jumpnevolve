package com.googlecode.jumpnevolve.editor.arguments;

import com.googlecode.jumpnevolve.editor.Editor2;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.gui.DialogPart;

/**
 * @author Erik Wagner
 * 
 *         TODO: Andere Subklassen als PositionMarker erstellen
 */
public abstract class EditorArgument implements Pollable, Drawable {

	private final Editor2 editor;

	public abstract String getArgumentPart();

	public abstract void initialize(String value);

	public abstract EditorArgument getClone(Editor2 editor);

	public abstract void setArguments(EditorArgument parentArgs[]);

	public abstract DialogPart getDialogPart();

	public EditorArgument(Editor2 editor) {
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
	public Editor2 getEditor() {
		return this.editor;
	}
}
