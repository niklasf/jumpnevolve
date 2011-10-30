package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 *
 */
class Cannonball extends Shot {

	/**
	 * @param world
	 * @param shape
	 * @param livingTime
	 * @param shotDirection
	 * @param shotSpeed
	 */
	public Cannonball(World world, Vector position, Vector shotDirection) {
		super(world, ShapeFactory.createCircle(position, 10), 10.0f,
				shotDirection, 200.0f);
	}

	public Cannonball(World world, Vector position, String arguments) {
		this(world, position, Vector.parseVector(arguments));
	}

	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("/object-pictures/cannonball.png"));
	}
}
