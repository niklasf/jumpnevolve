package com.googlecode.jumpnevolve.game.physic;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

public class Follower extends AbstractObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 336126625516157155L;

	private final AbstractObject toFollow;
	private LinkedList<Vector> points = new LinkedList<Vector>();
	private final int maxSize;

	public Follower(World world, AbstractObject toFollow, int maxSize) {
		super(world, ShapeFactory.createCircle(Vector.ZERO, 1));
		this.toFollow = toFollow;
		this.maxSize = maxSize;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		this.points.addFirst(this.toFollow.getPosition());
		if(this.points.size() > this.maxSize){
			this.points.removeLast();
		}
	}

	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.yellow);
		for (int i = 0; i < points.size() - 1; i++) {
			GraphicUtils.drawLine(g, points.get(i), points.get(i + 1));
		}
		g.setColor(c);
	}
}
