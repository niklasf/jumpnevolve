package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class Cannonball extends Shot {

	/**
	 * @param world
	 * @param shape
	 * @param livingTime
	 * @param shotDirection
	 * @param shotSpeed
	 */
	public Cannonball(World world, Vector position, Vector shotDirection) {
		super(world, new Circle(position, 10.0f), 10.0f, shotDirection, 500.0f);
	}

	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("/object-pictures/cannonball.png"));
	}
}
