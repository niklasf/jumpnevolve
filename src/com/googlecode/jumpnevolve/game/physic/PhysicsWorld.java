package com.googlecode.jumpnevolve.game.physic;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.world.World;

public class PhysicsWorld extends World {

	private final float frameInterval;
	
	public PhysicsWorld(int width, int height, int subareaWidth, float frameInterval) {
		super(width, height, subareaWidth);
		this.frameInterval = frameInterval;
	}
	
	protected void drawBackground(Graphics g) {
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, frameInterval);
	}
}
