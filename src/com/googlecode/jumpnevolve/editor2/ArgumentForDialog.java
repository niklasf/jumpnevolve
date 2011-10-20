package com.googlecode.jumpnevolve.editor2;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.gui.Contentable;
import com.googlecode.jumpnevolve.graphics.gui.DialogPart;

public abstract class ArgumentForDialog extends EditorArgument {

	protected Contentable part;
	public final String name;

	public ArgumentForDialog(String name, Contentable part) {
		super();
		this.name = name;
		this.part = part;
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

	public DialogPart getInterfacePart() {
		return new DialogPart(this.part, this.name);
	}
}
