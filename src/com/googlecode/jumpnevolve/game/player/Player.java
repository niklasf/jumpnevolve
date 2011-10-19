package com.googlecode.jumpnevolve.game.player;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.LimitedObjectFocusingCamera;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.gui.ButtonList;
import com.googlecode.jumpnevolve.graphics.gui.GridContainer;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceButton;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.Interfaceable;
import com.googlecode.jumpnevolve.graphics.gui.MainGUI;
import com.googlecode.jumpnevolve.graphics.world.Camera;
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
public class Player implements Pollable, Interfaceable {

	private PlayerFigure figure;
	private Playable cur;
	private HashMap<InterfaceFunction, Playable> figureList = new HashMap<InterfaceFunction, Playable>();
	private final Level parent;
	private final MainGUI gui;
	private Vector save;

	public Player(Level parent, Vector startPosition, String avaiableFigures,
			String startFigure, String[] savePositions, boolean cameraOnPlayer) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		this.gui = new MainGUI(this);

		GridContainer grid = new GridContainer(3, 3);
		ButtonList selectList = new ButtonList(2, 10);
		grid.add(selectList, 0, 2, GridContainer.MODUS_X_RIGHT,
				GridContainer.MODUS_Y_UP);
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.FIGURE_ROLLING_BALL,
				"object-pictures/figure-rolling-ball.png"));
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.FIGURE_JUMPING_CROSS,
				"object-pictures/figure-cross.png"));
		gui.setMainContainer(grid);

		this.figure = new PlayerFigure(parent, startPosition, this);
		setFigures(avaiableFigures, startFigure);
		this.setActivSavepoint(this.figure.getPosition());
		this.parent.add(this.figure);
		if (cameraOnPlayer) {
			this.parent.setCamera(new LimitedObjectFocusingCamera(this.figure));
		}
		for (String string : savePositions) {
			this.parent.add(new SavePoint(this.parent, Vector
					.parseVector(string), this));
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		gui.poll(input, secounds);
		if (this.figure.getShape().getUpperEnd() > this.parent.height) {
			this.figure.setPosition(this.getLastSave());
			this.figure.stopMoving();
		}
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

	private void setFigures(String avaiableFigures, String startFigure) {
		String figures[] = avaiableFigures.split(",");
		for (String figure : figures) {
			InterfaceFunction curNum = InterfaceFunctions.ERROR;
			if (figure.equals("RollingBall")) {
				curNum = InterfaceFunctions.FIGURE_ROLLING_BALL;
			} else if (figure.equals("JumpingCross")) {
				curNum = InterfaceFunctions.FIGURE_JUMPING_CROSS;
			}
			this.figureList.put(curNum, this.getNewFigure(curNum));
		}
		InterfaceFunction curNum = InterfaceFunctions.ERROR;
		if (startFigure.equals("RollingBall")) {
			curNum = InterfaceFunctions.FIGURE_ROLLING_BALL;
		} else if (startFigure.equals("JumpingCross")) {
			curNum = InterfaceFunctions.FIGURE_JUMPING_CROSS;
		}
		this.cur = this.figureList.get(curNum);
		this.figure.setShape(this.cur.getShape());
	}

	private Playable getNewFigure(InterfaceFunction number) {
		if (number == InterfaceFunctions.FIGURE_ROLLING_BALL) {
			return new RollingBall(this.figure);
		} else if (number == InterfaceFunctions.FIGURE_JUMPING_CROSS) {
			return new Basic(this.figure); // TODO: JumpingCross erstellen und
			// zurückgeben
		} else if (number == InterfaceFunctions.ERROR) {
			return new Basic(this.figure); // TODO: Fehlermeldung ausgeben
		} else {
			return new Basic(this.figure); // TODO: Fehlermeldung ausgeben
		}
	}

	public void changeFigure(InterfaceFunctions newFigure) {
		this.cur = this.figureList.get(newFigure);
		this.figure.setShape(this.cur.getShape());
	}

	public void activateSkill(int skill) {
		// TODO: Methode füllen
	}

	@Override
	public Camera getCamera() {
		return this.parent.getCamera();
	}

	@Override
	public int getHeight() {
		return Engine.getInstance().getHeight();
	}

	@Override
	public int getWidth() {
		return Engine.getInstance().getWidth();
	}

	@Override
	public float getZoomX() {
		return this.parent.getZoomX();
	}

	@Override
	public float getZoomY() {
		return this.parent.getZoomY();
	}

	@Override
	public void draw(Graphics g) {
		gui.draw(g);
	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		InterfaceFunction function = object.getFunction();
		if (function == InterfaceFunctions.FIGURE_ROLLING_BALL
				|| function == InterfaceFunctions.FIGURE_JUMPING_CROSS) {
			this.changeFigure((InterfaceFunctions) function);
		}
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		// Vorerst nichts tun
	}

	@Override
	public void objectIsSelected(InterfaceObject object) {
		// Nichts tun
	}

	public Vector getLastSave() {
		return this.save;
	}

	void setActivSavepoint(Vector position) {
		this.save = position;
	}
}
