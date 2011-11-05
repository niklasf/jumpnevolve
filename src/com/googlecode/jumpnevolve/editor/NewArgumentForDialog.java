package com.googlecode.jumpnevolve.editor;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.gui.Contentable;
import com.googlecode.jumpnevolve.graphics.gui.DialogPart;

public abstract class NewArgumentForDialog extends NewEditorArgument {

	protected Contentable part;
	public final String name;

	public NewArgumentForDialog(Editor2 editor, Contentable part, String name) {
		super(editor);
		this.part = part;
		this.name = name;
	}

	@Override
	public void poll(Input input, float secounds) {
		// Nichts tun
	}

	@Override
	public void draw(Graphics g) {
		// Nichts tun
	}

	@Override
	public String getArgumentPart() {
		return this.part.getContent();
	}

	@Override
	public void initialize(String value) {
		this.part.setContent(value);
	}

	@Override
	public DialogPart getDialogPart() {
		return new DialogPart(this.part, this.name);
	}

	@Override
	public void setArguments(NewEditorArgument[] parentArgs) {
		// Nichts tun, da keine Argumente benötigt werden
	}

}
