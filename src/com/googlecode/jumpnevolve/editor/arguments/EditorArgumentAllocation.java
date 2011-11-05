package com.googlecode.jumpnevolve.editor.arguments;

public class EditorArgumentAllocation {

	public final EditorArgument arg;
	public final int[] references;

	public EditorArgumentAllocation(EditorArgument arg, int[] references) {
		this.arg = arg;
		this.references = references;
	}

}
