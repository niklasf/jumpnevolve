/**
 * 
 */
package com.googlecode.jumpnevolve.editor2;

/**
 * @author Erik Wagner
 * 
 */
public class EditorArguments {

	private final int vectors, xFloats, yFloats, floats, booleans;
	private final String[] vectorNames, xFloatNames, yFloatNames, floatNames,
			booleanNames;

	public EditorArguments() {
		this(0, 0, 0, 0, 0, new String[0], new String[0], new String[0],
				new String[0], new String[0]);
	}

	public EditorArguments(int vectors, int xFloats, int yFloats, int floats,
			int booleans, String[] vectorNames, String[] xFloatNames,
			String[] yFloatNames, String[] floatNames, String[] booleanNames) {
		this.vectors = vectors;
		this.xFloats = xFloats;
		this.yFloats = yFloats;
		this.floats = floats;
		this.booleans = booleans;
		this.vectorNames = vectorNames;
		this.xFloatNames = xFloatNames;
		this.yFloatNames = yFloatNames;
		this.floatNames = floatNames;
		this.booleanNames = booleanNames;
	}

	public EditorArguments(int vectors, String[] vectorNames) {
		this(vectors, 0, 0, 0, 0, vectorNames, new String[0], new String[0],
				new String[0], new String[0]);
	}

	public EditorObjectInterface getEditorObjectInterface() {
		EditorObjectInterface re = new EditorObjectInterface();
		return re;
	}
}
