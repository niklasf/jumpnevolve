package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

/**
 * Die Vorlage für jede Art von Landschaft.
 * 
 * Landschaft ziechnet sich dadurch aus, dass sie sich niemals verändert
 * (unbeweglich, tot, nicht tötungsfähig und nicht aktivierbar). Sie sind jedoch
 * blockbar, was bedeutet, dass sie andere Objekte blkockieren können.
 * 
 * @author Erik Wagner
 * 
 */
public abstract class GroundTemplate extends AbstractObject {

	public GroundTemplate(World world, Shape shape) {
		super(world, shape, 0.0f, true, false, false, false, false);
	}

}
