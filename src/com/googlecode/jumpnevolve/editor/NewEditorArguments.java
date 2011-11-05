package com.googlecode.jumpnevolve.editor;

import java.util.ArrayList;

public class NewEditorArguments {

	private ArrayList<NewEditorArgumentAllocation> args = new ArrayList<NewEditorArgumentAllocation>();

	public NewEditorArguments(NewEditorArgumentAllocation[] args) {
		if (args != null) {
			for (NewEditorArgumentAllocation arg : args) {
				this.args.add(arg);
			}
		}
	}

	public void initObject(EditorObject obj) {
		for (NewEditorArgumentAllocation allo : this.args) {
			obj.addNewArgument(allo.arg.getClone(obj.parent,
					this.getArguments(allo.references, obj)));
		}
	}

	private NewEditorArgument[] getArguments(int[] references, EditorObject obj) {
		NewEditorArgument[] re = new NewEditorArgument[references.length];
		for (int i = 0; i < references.length; i++) {
			re[i] = this.getArguments(i, obj);
		}
		return re;
	}

	private NewEditorArgument getArguments(int i, EditorObject obj) {
		if (i < 0) {
			return obj.getPositionMarker();
		} else {
			return this.args.get(Math.min(i, this.args.size() - 1)).arg;
		}
	}
}
