package com.googlecode.jumpnevolve.game.menu;

import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.MainGUI;
import com.googlecode.jumpnevolve.graphics.gui.container.BorderContainer;
import com.googlecode.jumpnevolve.graphics.gui.container.GridContainer;
import com.googlecode.jumpnevolve.graphics.gui.container.TextButtonList;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceTextButton;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Menü zum Auswählen aus vorhandenen Leveln
 * 
 * @author Erik Wagner
 * 
 */
public class LevelSelection extends SubMenu {

	private static int number = 0;
	private ArrayList<File> levels;
	private String currentClicked, currentOver, lastClicked;
	private boolean interfaceClicked, interfaceOver, lastRoundClicked;
	private TextButtonList selectList = new TextButtonList(6, 10);

	/**
	 * Erstellt ein neues Menü zum Auswählen eines Levels
	 * 
	 * @param levelPath
	 *            Der Ordner, in dem sich die Level befinden, es werden auch die
	 *            Unterordner durchsucht
	 */
	public LevelSelection(Menu parent, String levelPath) {
		super(parent, new GridContainer(1, 1), "LevelSelection"
				+ getNextNumber());
		this.levels = this.searchFiles(new File(levelPath), ".txt");
		this.levels.addAll(this.searchFiles(new File(levelPath), ".lvl"));
		for (File file : this.levels) {
			InterfaceTextButton button = new InterfaceTextButton(
					InterfaceFunctions.LEVELSELECTION, file.getName());
			button.addInformable(this);
			this.selectList.addTextButton(button);
		}
		GridContainer grid = new GridContainer(1, 1);
		grid.add(this.selectList, 0, 0);
		grid.maximizeSize();
		this.setMainContainer(grid);
	}

	private static String getNextNumber() {
		number++;
		return "" + number;
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
		super.poll(input, secounds);
		if (lastRoundClicked && interfaceOver
				&& currentOver.equals(lastClicked)) {
			String load = this.getFileName(currentOver);
			if (load != null) {
				Level newLevel = Levelloader.asyncLoadLevel(load);
				newLevel.getPlayer().setParentMenu(this.parent);
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

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		super.mouseClickedAction(object);
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

	@Override
	public void objectIsSelected(InterfaceObject object) {
		// Nichts tun
	}
}
