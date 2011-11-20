package com.googlecode.jumpnevolve.editor;

import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.math.Vector;

public class EditorCamera implements Camera {

	private static final long serialVersionUID = 4544520375814328603L;

	private final Editor parent;

	public EditorCamera(Editor parent) {
		this.parent = parent;
	}

	@Override
	public Vector getPosition() {
		return this.parent.getCameraPos();
	}

}
