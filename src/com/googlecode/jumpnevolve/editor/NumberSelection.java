package com.googlecode.jumpnevolve.editor;

import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceNumberSelection;

public class NumberSelection extends ArgumentForDialog {

	private final int min, max, step;

	public NumberSelection(String name, int min, int max, int start, int step) {
		super(name, new InterfaceNumberSelection(min, max, start, step));
		this.min = min;
		this.max = max;
		this.step = step;
	}

	@Override
	public EditorArgument clone() {
		return new NumberSelection(this.name, this.min, this.max,
				Integer.parseInt(this.part.getContent()), this.step);
	}
}
