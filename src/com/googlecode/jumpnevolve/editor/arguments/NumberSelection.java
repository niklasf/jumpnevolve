package com.googlecode.jumpnevolve.editor.arguments;

import com.googlecode.jumpnevolve.editor.Editor2;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceNumberSelection;

public class NumberSelection extends ArgumentForDialog {

	private final int min, max, step;

	public NumberSelection(Editor2 editor, String name, int start, int min,
			int max, int step) {
		super(editor, new InterfaceNumberSelection(min, max, start, step), name);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	@Override
	public EditorArgument getClone(Editor2 editor) {
		return new NumberSelection(editor, this.name, Integer.parseInt(this
				.getArgumentPart()), this.min, this.max, this.step);
	}
}
