package com.googlecode.jumpnevolve.game;

import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.googlecode.jumpnevolve.graphics.AbstractState;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.gui.BorderContainer;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceTextButton;
import com.googlecode.jumpnevolve.graphics.gui.Interfaceable;
import com.googlecode.jumpnevolve.graphics.gui.MainGUI;
import com.googlecode.jumpnevolve.graphics.gui.TextButtonList;
import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Menü zum Auswählen aus vorhandenen Leveln
 * 
 * @author Erik Wagner
 * 
 */
public class LevelSelection extends AbstractState implements Interfaceable {

	private ArrayList<File> levels;
	private MainGUI gui;
	private String currentClicked, currentOver, lastClicked;
	private boolean interfaceClicked, interfaceOver, lastRoundClicked;

	/**
	 * Erstellt ein neues Menü zum Auswählen eines Levels
	 * 
	 * @param levelPath
	 *            Der Ordner, in dem sich die Level befinden, es werden auch die
	 *            Unterordner durchsucht
	 */
	public LevelSelection(String levelPath) {
		this.levels = this.searchFiles(new File(levelPath), ".txt");
		this.levels.addAll(this.searchFiles(new File(levelPath), ".lvl"));
		this.gui = new MainGUI(this);
		TextButtonList selectList = new TextButtonList(6, 10);
		BorderContainer border = new BorderContainer();
		border.add(selectList, BorderContainer.POSITION_MIDDLE);
		for (File file : levels) {
			selectList.addTextButton(new InterfaceTextButton(
					InterfaceFunctions.LEVELSELECTION, file.getName()));
		}
		gui.setMainContainer(border);
	}

	private ArrayList<File> searchFiles(File dir, String find) {
		File[] files = dir.listFiles();
		ArrayList<File> matches = new ArrayList<File>();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith(find)) {
					matches.add(files[i]);
				}
				if (files[i].isDirectory()) {
					matches.addAll(searchFiles(files[i], find));
				}
			}
		}
		return matches;
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
		lastRoundClicked = interfaceClicked;
		lastClicked = currentClicked;
		currentClicked = "";
		interfaceClicked = false;
		interfaceOver = false;
		this.gui.poll(input, secounds);
		if (lastRoundClicked && interfaceOver
				&& currentOver.equals(lastClicked)) {
			String load = this.getFileName(currentOver);
			if (load != null) {
				Level newLevel = Levelloader.asyncLoadLevel(load);
				Engine.getInstance().switchState(newLevel);
			}
		}
	}

	private String getFileName(String levelName) {
		for (File file : this.levels) {
			if (file.getName().equals(levelName)) {
				return file.getPath();
			}
		}
		return null;
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
		this.gui.draw(g);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public Camera getCamera() {
		return new Camera() {
			@Override
			public Vector getPosition() {
				return new Vector(Engine.getInstance().getWidth() / 2.0f,
						Engine.getInstance().getHeight() / 2.0f);
			}
		};
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
	public void mouseClickedAction(InterfaceObject object) {
		if (object.getFunction() == InterfaceFunctions.LEVELSELECTION
				&& object instanceof InterfaceTextButton) {
			this.currentClicked = ((InterfaceTextButton) object).getText();
		}
		this.interfaceClicked = true;
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		this.interfaceOver = true;
		if (object.getFunction() == InterfaceFunctions.LEVELSELECTION
				&& object instanceof InterfaceTextButton) {
			this.currentOver = ((InterfaceTextButton) object).getText();
		}
	}

}
