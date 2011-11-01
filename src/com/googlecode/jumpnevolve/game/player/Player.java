package com.googlecode.jumpnevolve.game.player;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.LimitedObjectFocusingCamera;
import com.googlecode.jumpnevolve.game.menu.Menu;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.gui.BorderContainer;
import com.googlecode.jumpnevolve.graphics.gui.ButtonList;
import com.googlecode.jumpnevolve.graphics.gui.Dialog;
import com.googlecode.jumpnevolve.graphics.gui.GridContainer;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceButton;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceContainer;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceTextButton;
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
	private Playable curPlayable;
	private HashMap<InterfaceFunction, Playable> figureList = new HashMap<InterfaceFunction, Playable>();
	private final Level parent;
	private final MainGUI gui;
	private Vector save;
	private Dialog finishDialog;
	private Menu parentMenu = null;
	private boolean menuButtonCreated = false;

	public Player(Level parent, Vector startPosition, String avaiableFigures,
			String startFigure, boolean cameraOnPlayer) {
		this.parent = parent;
		this.gui = new MainGUI(this);
		this.gui.maximizeSize();

		// Liste mit Spielfiguren erstellen
		ButtonList selectList = new ButtonList(2, 10);
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.FIGURE_ROLLING_BALL,
				"object-pictures/figure-rolling-ball.png"));
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.FIGURE_JUMPING_CROSS,
				"object-pictures/figure-cross.png"));

		// Finish-Dialog erstellen
		this.finishDialog = new Dialog();
		this.finishDialog.addTextButton(InterfaceFunctions.LEVEL_EXIT,
				"Level beenden");

		// GridContainer für das Interface erstellen und setzen
		GridContainer grid = new GridContainer(3, 3);
		grid.add(selectList, 0, 2, GridContainer.MODUS_X_RIGHT,
				GridContainer.MODUS_Y_UP);
		grid.add(this.finishDialog, 2, 1);
		grid.maximizeSize();
		this.gui.setMainContainer(grid);

		this.figure = new PlayerFigure(parent, startPosition, this);
		this.setFigures(avaiableFigures, startFigure);
		this.setActivSavepoint(this.figure.getPosition());
		this.parent.add(this.figure);
		if (cameraOnPlayer) {
			this.parent.setCamera(new LimitedObjectFocusingCamera(this.figure));
		}
	}

	public Player(Menu menu, Level parent, Vector startPosition,
			String avaiableFigures, String startFigure, boolean cameraOnPlayer) {
		this(parent, startPosition, avaiableFigures, startFigure,
				cameraOnPlayer);
		this.parentMenu = menu;
	}

	@Override
	public void poll(Input input, float secounds) {
		this.gui.poll(input, secounds);
		if (this.figure.getShape().getUpperEnd() > this.parent.height) {
			this.figure.setPosition(this.getLastSave());
			this.figure.stopMoving();
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			this.figure.jump();
		}
		if (input.isKeyDown(Input.KEY_RIGHT)
				&& input.isKeyDown(Input.KEY_LEFT) == false) {
			this.figure.run(Playable.DIRECTION_RIGHT);
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			this.figure.run(Playable.DIRECTION_LEFT);
		} else {
			this.figure.run(Playable.STAY);
		}
	}

	public PlayerFigure getFigure() {
		return this.figure;
	}

	public Playable getCurPlayable() {
		return this.curPlayable;
	}

	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
		this.createMenuButton();
	}

	public Menu getParentMenu() {
		return this.parentMenu;
	}

	private void createMenuButton() {
		if (!this.menuButtonCreated) {
			InterfaceContainer mainCon = this.gui.getMainContainer();
			if (mainCon instanceof GridContainer) {
				((GridContainer) mainCon).add(new InterfaceTextButton(
						InterfaceFunctions.LEVEL_EXIT, "Hauptmenü"), 0, 1,
						GridContainer.MODUS_DEFAULT, GridContainer.MODUS_Y_UP);
			} else if (mainCon instanceof BorderContainer) {
				((BorderContainer) mainCon).add(new InterfaceTextButton(
						InterfaceFunctions.LEVEL_EXIT, "Hauptmenü"),
						BorderContainer.POSITION_HIGH);
			}
			this.menuButtonCreated = true;
		}
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
		this.curPlayable = this.figureList.get(curNum);
		this.figure.setShape(this.curPlayable.getShape());
	}

	private Playable getNewFigure(InterfaceFunction number) {
		if (number == InterfaceFunctions.FIGURE_ROLLING_BALL) {
			return new RollingBall(this.figure);
		} else if (number == InterfaceFunctions.FIGURE_JUMPING_CROSS) {
			return new Basic(this.figure);
			// TODO: JumpingCross erstellen und zurückgeben
		} else if (number == InterfaceFunctions.ERROR) {
			return new Basic(this.figure); // TODO: Fehlermeldung ausgeben
		} else {
			return new Basic(this.figure); // TODO: Fehlermeldung ausgeben
		}
	}

	public void changeFigure(InterfaceFunctions newFigure) {
		this.curPlayable = this.figureList.get(newFigure);
		this.figure.setShape(this.curPlayable.getShape());
	}

	public void activateSkill(int skill) {
		// TODO: Methode füllen (activateSkill)
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
		this.gui.draw(g);
	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		InterfaceFunction function = object.getFunction();
		if (function == InterfaceFunctions.FIGURE_ROLLING_BALL
				|| function == InterfaceFunctions.FIGURE_JUMPING_CROSS) {
			this.changeFigure((InterfaceFunctions) function);
		} else if (function == InterfaceFunctions.LEVEL_EXIT) {
			this.exitLevel();
		}
	}

	private void exitLevel() {
		if (this.parentMenu != null) {
			this.parentMenu.switchBackToMainState();
			Engine.getInstance().switchState(this.parentMenu);
		} else {
			// TODO: Programm direkt beenden
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

	public void setActivSavepoint(Vector position) {
		this.save = position;
	}

	public void onFinish() {
		this.finishDialog.show();
	}
}
