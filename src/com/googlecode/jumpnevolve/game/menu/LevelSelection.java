package com.googlecode.jumpnevolve.game.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.container.GridContainer;
import com.googlecode.jumpnevolve.graphics.gui.container.TextButtonList;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceTextButton;
import com.googlecode.jumpnevolve.util.JarHandler;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * Menü zum Auswählen aus vorhandenen Leveln
 * 
 * @author Erik Wagner
 * 
 */
public class LevelSelection extends SubMenu {

	private static int number = 0;
	private ArrayList<String> levels;
	private String currentClicked, currentOver, lastClicked;
	private boolean interfaceClicked, interfaceOver, lastRoundClicked;
	private TextButtonList selectList = new TextButtonList(6, 10);
	private final String levelPath;

	/**
	 * Erstellt ein neues Menü zum Auswählen eines Levels
	 * 
	 * @param levelPath
	 *            Der Ordner, in dem sich die Level befinden, es werden auch die
	 *            Unterordner durchsucht
	 */
	public LevelSelection(Menu parent, String levelPath, String name) {
		super(parent, new GridContainer(1, 1), name);

		this.levels = new ArrayList<String>();

		if (levelPath.startsWith(Parameter.PROGRAMM_DIRECTORY_LEVELS)) {
			// Level auf normale Art lokalisieren, wenn im Userverzeichnis
			// gesucht werden soll
			this.levelPath = levelPath;
			this.levels = this.defaultFileSearch();
		} else {
			if (JarHandler.existJar()) {

				this.levelPath = "/" + levelPath;

				// Wenn das Jar-Archiv existiert, dann Level aus dem Archiv
				// laden
				this.levels = this.jarFileSearch();

			} else {
				// Levelpfad um "resources/" ergänzen, wenn das Programm nicht
				// aus einem Jar-Archiv geladen
				if (!levelPath.startsWith("resources/")) {
					levelPath = "resources/" + levelPath;
				}
				this.levelPath = levelPath;

				// Wenn Laden der Level aus Jar-Archiv nicht funktioniert, Level
				// nach normalem Schema laden
				this.levels = this.defaultFileSearch();
			}
		}

		// Button-Liste erstellen
		for (String file : this.levels) {
			InterfaceTextButton button = new InterfaceTextButton(
					InterfaceFunctions.LEVELSELECTION, file);
			button.addInformable(this);
			this.selectList.addTextButton(button);
		}
		GridContainer grid = new GridContainer(1, 1);
		grid.add(this.selectList, 0, 0);
		grid.maximizeSize();
		this.setMainContainer(grid);
	}

	private ArrayList<String> jarFileSearch() {
		ArrayList<String> re = new ArrayList<String>();

		JarFile jFile = JarHandler.getJarFile();
		Enumeration<JarEntry> jEntries = jFile.entries();

		while (jEntries.hasMoreElements()) {
			JarEntry jEntry = jEntries.nextElement();
			if (jEntry.getName().endsWith(".txt")
					|| jEntry.getName().endsWith(".lvl")) {
				String entryName = jEntry.getName();
				if (entryName.startsWith(this.levelPath.substring(1))) {
					entryName = entryName.replaceAll(
							this.levelPath.substring(1), "");
					re.add(entryName);
				}
			}
		}
		return re;
	}

	private ArrayList<String> defaultFileSearch() {
		ArrayList<String> re = new ArrayList<String>();

		re.addAll(this.searchFiles(new File(this.levelPath), ".txt"));
		re.addAll(this.searchFiles(new File(this.levelPath), ".lvl"));

		return re;
	}

	public LevelSelection(Menu parent, String levelPath) {
		this(parent, levelPath, "LevelSelection" + getNextNumber());
	}

	private static String getNextNumber() {
		number++;
		return "" + number;
	}

	private ArrayList<String> searchFiles(File dir, String find) {
		File[] files = dir.listFiles();
		ArrayList<String> matches = new ArrayList<String>();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith(find)) {
					matches.add(files[i].getName());
				}
				if (files[i].isDirectory()) {
					matches.addAll(searchFiles(files[i], find));
				}
			}
		}
		return matches;
	}

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
		if (!levelName.startsWith(levelPath)) {
			levelName = levelPath + levelName;
		}
		return levelName;
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
