package com.googlecode.jumpnevolve.game.player;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Playable;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

public class RollingBall implements Playable {

	private final PlayerFigure parent;

	public RollingBall(PlayerFigure parent) {
		this.parent = parent;
	}

	@Override
	public float getJumpingHeight() {
		// TODO Auto-generated method stub
		return 0.25f;
	}

	@Override
	public float getRunningSpeed() {
		// TODO Auto-generated method stub
		return 100;
	}

	@Override
	public boolean isBlockable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isKillable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isLiving() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isPushable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.parent.getShape(), ResourceManager
				.getInstance().getImage(
						"object-pictures/figure-rolling-ball.png"));
	}

	@Override
	public Shape getShape() {
		return new Circle(Vector.ZERO, 30.0f);
	}

}
