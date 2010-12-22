package com.googlecode.jumpnevolve.game.player;

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.game.Playable;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

public class Basic implements Playable {

	@Override
	public float getJumpingHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRunningSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Shape getShape() {
		// TODO Auto-generated method stub
		return new Circle(Vector.ZERO, 10.0f);
	}

	@Override
	public boolean isBlockable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isKillable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLiving() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPushable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub

	}

}
