package com.googlecode.jumpnevolve.editor;

import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceCheckbox;

public class NewCheckbox extends NewArgumentForDialog {

	public NewCheckbox(Editor2 editor, String name, boolean status) {
		super(editor, new InterfaceCheckbox(InterfaceFunctions.ERROR, status),
				name);
	}

	@Override
	public NewEditorArgument getClone(Editor2 editor,
			NewEditorArgument[] parentArgs) {
		return new NewCheckbox(editor, this.name, Boolean.parseBoolean(this
				.getArgumentPart()));
	}
}
