package com.googlecode.jumpnevolve.editor;

import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceNumberSelection;

public class NewNumberSelection extends NewArgumentForDialog {

	private final int min, max, step;

	public NewNumberSelection(Editor2 editor, String name, int start, int min,
			int max, int step) {
		super(editor, new InterfaceNumberSelection(min, max, start, step), name);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	@Override
	public NewEditorArgument getClone(Editor2 editor,
			NewEditorArgument[] parentArgs) {
		return new NewNumberSelection(editor, this.name, Integer.parseInt(this
				.getArgumentPart()), this.min, this.max, this.step);
	}
}
