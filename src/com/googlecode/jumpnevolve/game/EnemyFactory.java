package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

/**
 * Die Vorlage für jede Art von Gegnern.
 * 
 * Ein Gegner zeichnet sich dadurch aus, dass er lebt und nicht geschoben oder
 * aktiviert werden kann. Er kann blockbar sein, muss es aber nicht.
 * 
 * @author Erik Wagner
 * 
 */
public abstract class EnemyFactory extends AbstractObject {

	public EnemyFactory(World world, Shape shape, float mass, boolean blockable) {
		super(world, shape, mass, blockable, false, true, false);
		// TODO Auto-generated constructor stub
	}

}
