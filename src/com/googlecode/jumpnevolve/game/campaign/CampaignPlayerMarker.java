package com.googlecode.jumpnevolve.game.campaign;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.math.Vector;

public class CampaignPlayerMarker implements Drawable, Pollable {

	private static final float MOVING_SPEED = 10.0f;

	private static final float MINIMUM_DISTANCE = 2.0f;

	private Vector position;
	private Vector targetPosition;

	public CampaignPlayerMarker(Vector position) {
		this.position = position;
		this.targetPosition = position;
	}

	public void moveTo(Vector position) {
		this.targetPosition = position;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void poll(Input input, float secounds) {
		if (this.position.getDistance(this.targetPosition) < MINIMUM_DISTANCE) {
			this.targetPosition = this.position;
		} else {
			this.position = this.targetPosition.sub(this.position)
					.getDirection().mul(MOVING_SPEED);
		}
	}

}
