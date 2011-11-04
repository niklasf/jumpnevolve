/**
 *
 */
package com.googlecode.jumpnevolve.editor;

import java.util.ArrayList;

/**
 * @author Erik Wagner
 *
 */
public class EditorArguments {

	private ArrayList<EditorArgument> args = new ArrayList<EditorArgument>();

	public EditorArguments(EditorArgument args[]) {
		if (args != null) {
			for (EditorArgument arg : args) {
				this.addArgument(arg);
			}
		}
	}

	public void addArgument(EditorArgument arg) {
		this.args.add(arg);
	}

	@SuppressWarnings("unchecked")
	public void initObject(EditorObject obj) {
		for (EditorArgument arg : (ArrayList<EditorArgument>) this.args.clone()) {
			obj.addArgument(arg.clone());
		}
	}
}
