package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine KillingMachine mit Kaktus-Aussehen
 *
 * @author Erik Wagner
 *
 */
public class Cactus extends KillingMachine {

	private static final long serialVersionUID = 3087869575259757544L;

	public Cactus(World world, Vector position) {
		super(world, position);
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.getShape(), ResourceManager
				.getInstance().getImage("object-pictures/cactus.png"));
	}
}
