package com.googlecode.jumpnevolve.editor.arguments;

import com.googlecode.jumpnevolve.editor.Editor;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceNumberSelection;

public class NumberSelection extends ArgumentForDialog {

	private final int min, max, step;

	public NumberSelection(Editor editor, String name, int start, int min,
			int max, int step) {
		super(editor, new InterfaceNumberSelection(min, max, start, step), name);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	@Override
	public EditorArgument getClone(Editor editor) {
		return new NumberSelection(editor, this.name, Integer.parseInt(this
				.getArgumentPart()), this.min, this.max, this.step);
	}
}
