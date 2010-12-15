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
public class Player implements Pollable {

	public static final int ROLLING_BALL = 0;
	public static final int JUMPING_CROSS = 1;
	public static final int HIGH_JUMP = 100;

	private FigureTemplate cur;
	private HashMap<Integer, FigureTemplate> figureList;
	private final Level parent;

	public Player(Level parent, String avaiableFigures, String startFigure) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		setFigures(avaiableFigures, startFigure);
	}

	@Override
	public void poll(Input input, float secounds) {
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

	public FigureTemplate getCurrentFigure() {
		return cur;
	}

	private void setFigures(String avaiableFigures, String startFigure) {
		// TODO: Methode füllen
	}

	public void changeFigure(int newFigure) {
		FigureTemplate next = figureList.get(newFigure);
		next.synchronize(cur);
		this.parent.removeFromAllLists(cur);
		cur = next;
		this.parent.add(cur);
	}

	public void activateSkill(int skill) {
		// TODO: Methode füllen
	}
}
