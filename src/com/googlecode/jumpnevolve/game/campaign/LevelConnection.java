package com.googlecode.jumpnevolve.game.campaign;

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.Drawable;

public class LevelConnection implements Drawable {

	public final LevelMarker pos1, pos2;

	public LevelConnection(LevelMarker pos1, LevelMarker pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos1.addConnection(this);
		this.pos2.addConnection(this);
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub

	}
}
