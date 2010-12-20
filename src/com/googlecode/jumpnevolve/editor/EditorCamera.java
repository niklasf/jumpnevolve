/**
 * 
 */
package com.googlecode.jumpnevolve.editor;

import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class EditorCamera implements Camera {

	private final Editor parent;

	/**
	 * 
	 * @param parent
	 */
	public EditorCamera(Editor parent) {
		this.parent = parent;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector getPosition() {
		return parent.getCameraPosition();
	}

}
