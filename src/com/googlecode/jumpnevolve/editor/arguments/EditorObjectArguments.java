package com.googlecode.jumpnevolve.editor.arguments;

import java.util.ArrayList;

import com.googlecode.jumpnevolve.editor.EditorObject;

public class EditorObjectArguments {

	private ArrayList<EditorArgumentAllocation> args = new ArrayList<EditorArgumentAllocation>();

	public EditorObjectArguments(EditorArgumentAllocation[] args) {
		if (args != null) {
			for (EditorArgumentAllocation arg : args) {
				this.args.add(arg);
			}
		}
	}

	public void initObject(EditorObject obj) {
		ArrayList<EditorArgumentAllocation> clonedArgs = new ArrayList<EditorArgumentAllocation>();

		for (EditorArgumentAllocation allo : this.args) {
			clonedArgs.add(new EditorArgumentAllocation(allo.arg
					.getClone(obj.parent), allo.references));
		}
		for (EditorArgumentAllocation allo : clonedArgs) {
			allo.arg.setArguments(this.getArguments(allo.references, obj,
					clonedArgs));
		}
		for (EditorArgumentAllocation allo : clonedArgs) {
			obj.addNewArgument(allo.arg);
		}
	}

	private EditorArgument[] getArguments(int[] references, EditorObject obj,
			ArrayList<EditorArgumentAllocation> args) {
		if (references != null) {
			EditorArgument[] re = new EditorArgument[references.length];
			for (int i = 0; i < references.length; i++) {
				re[i] = this.getArguments(references[i], obj, args);
			}
			return re;
		} else {
			return new EditorArgument[0];
		}
	}

	private EditorArgument getArguments(int i, EditorObject obj,
			ArrayList<EditorArgumentAllocation> args) {
		if (i < 0) {
			return obj.getPositionMarker();
		} else {
			return args.get(Math.min(i, args.size() - 1)).arg;
		}
	}
}
