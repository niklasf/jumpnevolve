/**
 * 
 */
package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class Cannon extends Shooter {

	/**
	 * @param world
	 * @param shape
	 * @param shotInterval
	 * @param activated
	 */
	public Cannon(World world, Vector position, Vector dimension,
			boolean activated) {
		super(world, new Rectangle(position, dimension), 10.0f, activated);
	}

	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("object-pictures/cannon.png"));
	}
}
