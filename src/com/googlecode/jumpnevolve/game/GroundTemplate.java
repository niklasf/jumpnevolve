package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

/**
 * Die Vorlage für jede Art von Landschaft.
 * 
 * Landschaft ziechnet sich dadurch aus, dass sie sich niemals verändert
 * (unblockbar, unbeweglich, tot und nicht aktivierbar)
 * 
 * @author Erik Wagner
 * 
 */
public abstract class GroundTemplate extends AbstractObject {

	public GroundTemplate(World world, Shape shape) {
		super(world, shape);
		// TODO Auto-generated constructor stub
	}

}
