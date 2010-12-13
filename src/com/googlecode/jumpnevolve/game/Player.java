package com.googlecode.jumpnevolve.game;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;

/**
 * 
 * @author Erik Wagner
 * 
 */
public class Player implements SpecialPollable, Drawable {

	private static final int ROLLING_BALL = 0;
	private static final int JUMPING_CROOS = 1;

	private FigureTemplate cur;
	private HashMap<Integer, FigureTemplate> figureList;

	public Player(String avaiableFigures, String startFigure) {
		// TODO Auto-generated constructor stub
		setFigures(avaiableFigures, startFigure);
	}

	public void startRound(Input input) {
		cur.startRound(input);
	}

	@Override
	public void poll(Input input, float secounds) {
		cur.poll(input, secounds);
		if (input.isKeyDown(Input.KEY_UP)) {
			cur.jump();
		}
		if (input.isKeyDown(Input.KEY_RIGHT)
				&& input.isKeyDown(Input.KEY_LEFT) == false) {
			cur.run(Playable.DIRECTION_RIGHT);
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			cur.run(Playable.DIRECTION_LEFT);
		}
	}

	public void endRound() {
		cur.endRound();
	}

	@Override
	public void draw(Graphics g) {
		cur.draw(g);
	}

	private void setFigures(String avaiableFigures, String startFigure) {
		// TODO: Methode füllen
	}

	private void changeFigure(int newFigure) {
		FigureTemplate next = figureList.get(newFigure);
		next.synchronize(cur);
		cur = next;
	}
}
