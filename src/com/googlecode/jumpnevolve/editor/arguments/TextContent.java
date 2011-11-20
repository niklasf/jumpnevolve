package com.googlecode.jumpnevolve.editor.arguments;

import com.googlecode.jumpnevolve.editor.Editor2;
import com.googlecode.jumpnevolve.graphics.gui.Contentable;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceTextField;

public class TextContent extends ArgumentForDialog {

	public TextContent(Editor2 editor, String name) {
		super(editor, new InterfaceTextField(InterfaceFunctions.ERROR), name);
	}

	@Override
	public EditorArgument getClone(Editor2 editor) {
		return new TextContent(editor, this.name);
	}

	@Override
	public String getArgumentPart() {
		String part = this.part.getContent();
		if (part.length() == 0) {
			return "Kein Inhalt";
		} else {
			return part;
		}
	}
}