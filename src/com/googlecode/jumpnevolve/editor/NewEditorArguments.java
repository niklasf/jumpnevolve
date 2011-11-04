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
					this.getArguments(allo.references)));
		}
	}

	private NewEditorArgument[] getArguments(int[] references) {
		NewEditorArgument[] re = new NewEditorArgument[references.length];
		for (int i = 0; i < references.length; i++) {
			re[i] = this.args.get(i).arg;
		}
		return re;
	}
}
