package com.googlecode.jumpnevolve.game.physic;

import java.util.ArrayList;

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
	private ArrayList<Vector> points = new ArrayList<Vector>();

	public Follower(World world, AbstractObject toFollow) {
		super(world, ShapeFactory.createCircle(Vector.ZERO, 1));
		this.toFollow = toFollow;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		this.points.add(this.toFollow.getPosition());
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
