package com.googlecode.jumpnevolve.game.player;

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine Basis Figur für den Spieler
 * 
 * @author Erik Wagner
 * 
 *         TODO: Werte anpassen
 */
public class Basic implements Playable {

	private final PlayerFigure parent;

	public Basic(PlayerFigure parent) {
		this.parent = parent;
	}

	@Override
	public float getJumpingHeight() {
		return 200.0f;
	}

	@Override
	public float getWalkingSpeed() {
		return 50;
	}

	@Override
	public NextShape getShape() {
		return ShapeFactory.createRectangle(Vector.ZERO, 20, 20);
	}

	@Override
	public boolean isBlockable() {
		return true;
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public boolean isLiving() {
		return true;
	}

	@Override
	public void draw(Graphics g) {
		// TODO: Bild ändern
		GraphicUtils.drawImage(g, this.parent.getShape(), ResourceManager
				.getInstance().getImage("object-pictures/figure-cross.png"));
	}

	@Override
	public float getElasticityFactor() {
		return 0;
	}

}
