package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

/**
 * Die Vorlage für alle Objekte jeder Art.
 * 
 * Objekte zeichnen sich dadurch aus, dass sie sich nicht selbst bewegen und tot
 * sind. Sie können schiebbar, blockbar und auch aktivierbar sein.
 * 
 * @author Erik Wagner
 * 
 */
public abstract class ObjectTemplate extends AbstractObject {

	public ObjectTemplate(World world, Shape shape, float mass,
			boolean blockable, boolean pushable, boolean activable) {
		super(world, shape, 0.0f, blockable, pushable, false, activable);
	}

}