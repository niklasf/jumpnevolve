package com.googlecode.jumpnevolve.game;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.player.Basic;
import com.googlecode.jumpnevolve.game.player.PlayerFigure;
import com.googlecode.jumpnevolve.game.player.RollingBall;
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

	private PlayerFigure figure;
	private Playable cur;
	private HashMap<Integer, Playable> figureList = new HashMap<Integer, Playable>();
	private final Level parent;
	private final Interface gui;

	public Player(Level parent, Vector startPosition, String avaiableFigures,
			String startFigure, String[] savePositions) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		this.gui = new Interface(this);
		this.figure = new PlayerFigure(parent, startPosition, this);
		setFigures(avaiableFigures, startFigure);
		this.parent.add(this.figure);
		// TODO: GUI initialisieren (addFunction())
		// TODO: savePositions verarbeiten
	}

	@Override
	public void poll(Input input, float secounds) {
		gui.poll(input, secounds);
		if (input.isKeyDown(Input.KEY_UP)) {
			figure.jump();
		}
		if (input.isKeyDown(Input.KEY_RIGHT)
				&& input.isKeyDown(Input.KEY_LEFT) == false) {
			figure.run(Playable.DIRECTION_RIGHT);
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			figure.run(Playable.DIRECTION_LEFT);
		} else {
			figure.run(Playable.STAY);
		}
	}

	public PlayerFigure getFigure() {
		return this.figure;
	}

	public Playable getCurPlayable() {
		return this.cur;
	}

	public Interface getInterface() {
		return this.gui;
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
		int curNum = ERROR;
		if (startFigure.equals("RollingBall")) {
			curNum = ROLLING_BALL;
		} else if (startFigure.equals("JumpingCross")) {
			curNum = JUMPING_CROSS;
		}
		this.cur = this.figureList.get(curNum);
		this.figure.setShape(this.cur.getShape());
	}

	private Playable getNewFigure(int number) {
		switch (number) {
		case ROLLING_BALL:
			return new RollingBall(this.figure);
		case JUMPING_CROSS:
			return new Basic(); // TODO: JumpingCross erstellen und hier
			// zurückgeben
		case ERROR:
		default:
			return new Basic(); // TODO: Fehler ausgeben
		}
	}

	public void changeFigure(int newFigure) {
		this.cur = this.figureList.get(newFigure);
		this.figure.setShape(this.cur.getShape());
	}

	public void activateSkill(int skill) {
		// TODO: Methode füllen
	}
}
