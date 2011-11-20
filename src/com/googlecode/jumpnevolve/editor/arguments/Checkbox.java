package com.googlecode.jumpnevolve.editor.arguments;

import com.googlecode.jumpnevolve.editor.Editor;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceCheckbox;

public class Checkbox extends ArgumentForDialog {

	public Checkbox(Editor editor, String name, boolean status) {
		super(editor, new InterfaceCheckbox(InterfaceFunctions.ERROR, status),
				name);
	}

	@Override
	public EditorArgument getClone(Editor editor) {
		return new Checkbox(editor, this.name, Boolean.parseBoolean(this
				.getArgumentPart()));
	}
}
