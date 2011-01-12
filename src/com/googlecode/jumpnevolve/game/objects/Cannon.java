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

	private Vector startPosition;
	private final Vector shotDirection;

	/**
	 * @param world
	 * @param shape
	 * @param shotInterval
	 * @param activated
	 */
	public Cannon(World world, Vector position, boolean activated,
			Vector shotDirection) {
		super(world, new Rectangle(position, 25.0f, 50.0f), 5.0f, activated);
		this.shotDirection = shotDirection.getDirection();
		this.startPosition = this.getPosition().add(
				new Vector(Math.abs(12.5f) * Math.signum(this.shotDirection.x),
						Math.abs(25.0f) * Math.signum(this.shotDirection.y)));
	}

	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("object-pictures/cannon.png"));
	}

	@Override
	protected void shot() {
		System.out.println("shooted!");
		this.getWorld().add(
				new Cannonball(this.getWorld(), this.startPosition,
						this.shotDirection));
	}
}
