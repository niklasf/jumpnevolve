package com.googlecode.jumpnevolve.game;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.objects.RollingBall;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Player liefert die Objekte (abgeleitet von FigureTemplate), die der Spieler
 * steuern kann und gibt diesen je nach Tastatur-Ereignissen "Anweisungen"
 * 
 * Außerdem bekommt Player die Ereignisse von Interface-Objekten und ruft die
 * entsprechenden Funktionen in den spielbaren Objekten auf
 * 
 * @author Erik Wagner
 * 
 */
public class Player implements Pollable {

	public static final int ERROR = -1;
	public static final int ROLLING_BALL = 0;
	public static final int JUMPING_CROSS = 1;
	public static final int HIGH_JUMP = 100;

	private FigureTemplate cur;
	private HashMap<Integer, FigureTemplate> figureList;
	private final Level parent;
	private final Vector startPostion;

	public Player(Level parent, Vector startPosition, String avaiableFigures,
			String startFigure) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		this.startPostion = startPosition;
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
		String figures[] = avaiableFigures.split(",");
		for (String figure : figures) {
			int curNum = ERROR;
			if (figure.equals("RollingBall")) {
				curNum = ROLLING_BALL;
			} else if (figure.equals("JumpingCross")) {
				curNum = JUMPING_CROSS;
			}
			this.figureList.put(curNum, this.getNewFigure(curNum));
		}
	}

	private FigureTemplate getNewFigure(int number) {
		switch (number) {
		case ROLLING_BALL:
			return new RollingBall(this.parent, this.startPostion);
		case JUMPING_CROSS:
			return null; // TODO: JumpingCross erstellen und hier zurückgeben
		case ERROR:
		default:
			return null; // TODO: Fehler ausgeben
		}
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
