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

	private static final long serialVersionUID = -2667560387023995791L;

	/**
	 * Erzeugt eine neue Kanonenkugel
	 * 
	 * @param world
	 *            Die Welt für das Objekt
	 * @param position
	 *            Die Position, an der die Kanonenkugel startet
	 * @param shotDirection
	 *            Die Richtung, in die sich die Kanonenkugel anfänglich bewegt
	 *            (--> wegen Schwerkraft)
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
