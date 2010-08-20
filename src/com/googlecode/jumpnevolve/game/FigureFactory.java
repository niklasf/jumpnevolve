package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

/**
 * Die Vorlage für alle vom Spieler gesteuerten Figuren.
 * 
 * Figuren zeichnen sich dadurch aus, dass sie sich bewegen, blockbar und lebend
 * sind. Sie können nich geschoben oder aktiviert werden.
 * 
 * @author Erik Wagner
 * 
 */
public abstract class FigureFactory extends AbstractObject {

	public FigureFactory(World world, Shape shape, float mass,
			boolean blockable, boolean living) {
		super(world, shape, mass, blockable, false, living, false);
	}
}
