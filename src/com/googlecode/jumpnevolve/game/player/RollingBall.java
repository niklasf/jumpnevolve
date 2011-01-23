package com.googlecode.jumpnevolve.game.player;

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

public class RollingBall implements Playable {

	private final PlayerFigure parent;

	public RollingBall(PlayerFigure parent) {
		this.parent = parent;
	}

	@Override
	public float getJumpingHeight() {
		// TODO Auto-generated method stub
		return 60.0f;
	}

	@Override
	public float getWalkingSpeed() {
		// TODO Auto-generated method stub
		return 100;
	}

	@Override
	public boolean isBlockable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isDamageable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isLiving() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.parent.getShape(), ResourceManager
				.getInstance().getImage(
						"object-pictures/figure-rolling-ball.png"));
	}

	@Override
	public NextShape getShape() {
		return ShapeFactory.createCircle(Vector.ZERO, 20.0f);
	}

}
