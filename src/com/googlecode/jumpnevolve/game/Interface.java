package com.googlecode.jumpnevolve.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.interfaceObjects.Button;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.ResourceManager;

/**
 * @author Erik Wagner
 * 
 */
public class Interface implements Pollable, Drawable {

	public static final int FIGURE_ROLLING_BALL = 0;
	public static final int FIGURE_JUMPING_CROSS = 1;
	public static final int SKILL_HIGH_JUMP = 100;

	private Button playerList[];
	private Button skillList[];
	private final Level parent;

	/**
	 * 
	 */
	public Interface(Level level) {
		this.parent = level;
		this.playerList = new Button[2];
		this.skillList = new Button[3];
		// TODO: Buttons erzeugen
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.graphics.Pollable#poll(org.newdawn.slick.Input
	 * , float)
	 */
	@Override
	public void poll(Input input, float secounds) {
		for (Button but : playerList) {
			but.poll(input, secounds);
		}
		for (Button but : skillList) {
			but.poll(input, secounds);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.graphics.Drawable#draw(org.newdawn.slick.Graphics
	 * )
	 */
	@Override
	public void draw(Graphics g) {
		for (Button but : playerList) {
			but.draw(g);
		}
		for (Button but : skillList) {
			but.draw(g);
		}
	}

	public void actionPerformed(int function) {
		switch (function) {
		case FIGURE_ROLLING_BALL:
			this.parent.getPlayer().changeFigure(Player.ROLLING_BALL);
			break;
		case FIGURE_JUMPING_CROSS:
			this.parent.getPlayer().changeFigure(Player.JUMPING_CROSS);
			break;
		case SKILL_HIGH_JUMP:
			this.parent.getPlayer().activateSkill(Player.HIGH_JUMP);
			break;
		default:
			break;
		}
	}
}
