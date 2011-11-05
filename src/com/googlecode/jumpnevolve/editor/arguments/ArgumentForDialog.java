package com.googlecode.jumpnevolve.editor.arguments;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.editor.Editor2;
import com.googlecode.jumpnevolve.graphics.gui.Contentable;
import com.googlecode.jumpnevolve.graphics.gui.DialogPart;

public abstract class ArgumentForDialog extends EditorArgument {

	protected Contentable part;
	public final String name;

	public ArgumentForDialog(Editor2 editor, Contentable part, String name) {
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
	public void setArguments(EditorArgument[] parentArgs) {
		// Nichts tun, da keine Argumente benötigt werden
	}

}
