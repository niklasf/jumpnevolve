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
		ArrayList<NewEditorArgumentAllocation> clonedArgs = new ArrayList<NewEditorArgumentAllocation>();

		for (NewEditorArgumentAllocation allo : this.args) {
			clonedArgs.add(new NewEditorArgumentAllocation(allo.arg
					.getClone(obj.parent), allo.references));
		}
		for (NewEditorArgumentAllocation allo : clonedArgs) {
			allo.arg.setArguments(this.getArguments(allo.references, obj,
					clonedArgs));
		}
		for (NewEditorArgumentAllocation allo : clonedArgs) {
			obj.addNewArgument(allo.arg);
		}
	}

	private NewEditorArgument[] getArguments(int[] references,
			EditorObject obj, ArrayList<NewEditorArgumentAllocation> args) {
		if (references != null) {
			NewEditorArgument[] re = new NewEditorArgument[references.length];
			for (int i = 0; i < references.length; i++) {
				re[i] = this.getArguments(references[i], obj, args);
			}
			return re;
		} else {
			return new NewEditorArgument[0];
		}
	}

	private NewEditorArgument getArguments(int i, EditorObject obj,
			ArrayList<NewEditorArgumentAllocation> args) {
		if (i < 0) {
			return obj.getPositionMarker();
		} else {
			return args.get(Math.min(i, args.size() - 1)).arg;
		}
	}
}
