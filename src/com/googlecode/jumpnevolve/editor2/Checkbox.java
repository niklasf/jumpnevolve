package com.googlecode.jumpnevolve.editor2;

import com.googlecode.jumpnevolve.graphics.gui.InterfaceCheckbox;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;

public class Checkbox extends ArgumentForDialog {

	public Checkbox(String name, boolean defaultValue) {
		super(name, new InterfaceCheckbox(InterfaceFunctions.ERROR,
				defaultValue));
	}

	@Override
	public EditorArgument clone() {
		return new Checkbox(this.name, Boolean.parseBoolean(this.part
				.getContent()));
	}
}
