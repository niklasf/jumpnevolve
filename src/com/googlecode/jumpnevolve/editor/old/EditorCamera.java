/**
 *
 */
package com.googlecode.jumpnevolve.editor.old;

import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine Kamera für den Editor, die sich ihre Position von ihrem Parent holt
 * 
 * @author Erik Wagner
 * 
 */
public class EditorCamera implements Camera {

	private final Editor parent;

	public EditorCamera(Editor parent) {
		this.parent = parent;
	}

	@Override
	public Vector getPosition() {
		return parent.getCameraPosition();
	}

}
