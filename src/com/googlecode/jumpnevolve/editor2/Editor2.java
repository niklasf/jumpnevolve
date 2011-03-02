package com.googlecode.jumpnevolve.editor2;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.GameObjects;
import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.gui.BorderContainer;
import com.googlecode.jumpnevolve.graphics.gui.ButtonList;
import com.googlecode.jumpnevolve.graphics.gui.Dialog;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceButton;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.Interfaceable;
import com.googlecode.jumpnevolve.graphics.gui.MainGUI;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class Editor2 extends Level implements Interfaceable {

	private static final int GUI_MODE_NONE = 0;
	private static final int GUI_MODE_OBJECT = 1;
	private static final int GUI_MODE_OTHER = 2;
	private ArrayList<EditorObject> objects = new ArrayList<EditorObject>();
	private EditorObject selected;
	private Vector cameraPos, oldCameraPos;
	private int curID;
	private MainGUI gui;
	private boolean guiAction;
	private boolean cameraMove;
	private Vector oldClick;
	private InterfaceFunction lastFunction;
	private int curGuiMode = GUI_MODE_NONE;
	private int lastGuiMode = GUI_MODE_NONE;
	private Dialog settings;

	/**
	 * @param loader
	 * @param width
	 * @param height
	 * @param subareaWidth
	 */
	public Editor2(Levelloader loader, int width, int height, int subareaWidth) {
		super(loader, width, height, subareaWidth);
		this.settings = new Dialog();
		this.settings.addTextField("Name");
		this.settings.addNumberSelection("Breite", 1, 100000);
		this.settings.addNumberSelection("Höhe", 1, 100000);
		this.settings.addTextField("Hintergrund");
		this.settings.addNumberSelection("Zeit", 1, 10000);
		this.settings.addNumberSelection("Zoom X", 1, 100);
		this.settings.addNumberSelection("Zoom Y", 1, 100);
		this.settings.addNumberSelection("Subarea-Breite", 1, 1000);
		// TODO: Maxima so in Ordnung?

		this.gui = new MainGUI(this);
		ButtonList selectList = new ButtonList(6, 10);
		BorderContainer border = new BorderContainer();
		border.add(selectList, BorderContainer.POSITION_LOW_LEFT);
		for (GameObjects obj : GameObjects.values()) {
			selectList.addButton(new InterfaceButton(obj,
					obj.editorSkinFileName));
		}
		border.add(this.settings, BorderContainer.POSITION_MIDDLE);
		gui.setMainContainer(border);
	}

	private void addNewObject(GameObjects function, Vector position) {
		String className = function.getClassNameForEditor();
		EditorObject obj = new EditorObject(this,
				this.getObjectName(className), className, position);
		function.editorArguments.initObject(obj);
		this.objects.add(obj);
		this.selected = obj;
		this.objects.add(obj);
	}

	private String getObjectName(String className) {
		this.curID++;
		return this.getTransformedId(this.curID);
	}

	private String getTransformedId(int value) {
		String id = "";
		if (value < 10) {
			id = id + "000" + value;
		} else if (value < 100) {
			id = id + "00" + value;
		} else if (value < 1000) {
			id = id + "0" + value;
		} else {
			id = id + value;
		}
		return id;
	}

	private float getCameraPosY() {
		return cameraPos.y;
	}

	private float getCameraPosX() {
		return cameraPos.x;
	}

	private void setCameraPosition(Vector newPos) {
		this.cameraPos = newPos;
	}

	private void setGuiMode(int guiMode) {
		this.lastGuiMode = this.curGuiMode;
		this.curGuiMode = guiMode;

	}

	public Vector translateMousePos(Vector mousePos) {
		return this.translateMousePos(mousePos.x, mousePos.y);
	}

	public Vector translateMousePos(float x, float y) {
		x = x - (float) this.getWidth() / 2;
		y = y - (float) this.getHeight() / 2;
		x = x / this.getZoomX();
		y = y / this.getZoomY();
		x = x + this.getCameraPosX();
		y = y + this.getCameraPosY();
		return new Vector(x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void poll(Input input, float secounds) {
		this.guiAction = false;
		this.gui.poll(input, secounds);
		Vector mousePos = new Vector(input.getMouseX(), input.getMouseY());
		Vector translatedMousePos = this.translateMousePos(mousePos);
		if (!guiAction) {
			if (this.lastGuiMode == GUI_MODE_OBJECT) {
				this.addNewObject((GameObjects) this.lastFunction,
						translatedMousePos);
			}
			this.lastGuiMode = GUI_MODE_NONE;
		}
		if (this.selected != null) {
			this.selected.poll(input, secounds);
		} else {
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				if (!this.guiAction) {
					if (!cameraMove) {
						this.oldClick = mousePos;
					}
					this.cameraMove = true;
					if (this.oldClick == null) {
						this.oldClick = mousePos;
					}
					Vector dif = mousePos.sub(oldClick);
					dif = dif.modifyX(dif.x / this.getZoomX());
					dif = dif.modifyY(dif.y / this.getZoomY());
					this.setCameraPosition(this.oldCameraPos.sub(dif));
				}
			}
		}
		if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
			for (EditorObject obj : (ArrayList<EditorObject>) this.objects
					.clone()) {
				if (obj.isPointIn(translatedMousePos)) {
					this.selected = obj;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		for (EditorObject obj : (ArrayList<EditorObject>) this.objects.clone()) {
			obj.draw(g);
		}
		this.selected.drawInterface(g);
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
		this.guiAction = true;
		InterfaceFunction function = object.getFunction();
		if (function instanceof GameObjects) {
			this.setGuiMode(GUI_MODE_OBJECT);
			this.lastFunction = object.getFunction();
		} else {
			this.setGuiMode(GUI_MODE_OTHER);
		}
		this.lastFunction = function;
		// TODO: Mit Inhalt füllen
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		this.guiAction = true;
		this.setGuiMode(GUI_MODE_NONE);
	}

	public void loadLevel(String path) throws IOException {
		BufferedReader levelFile = new BufferedReader(new FileReader(path));
		// Die ersten drei Zeilen laden
		String dimensionsLine = levelFile.readLine();
		String settingsLine = levelFile.readLine();
		String playerLine = levelFile.readLine();
		String[] dimensionsLineSplit = dimensionsLine.split("_");
		String[] settingsLineSplit = settingsLine.split("_");
		String[] playerLineSplit = playerLine.split("_");
		// Grundeinstellungen vornehmen
		if (dimensionsLineSplit[0].equals("Leveldimensionen")
				&& settingsLineSplit[0].equals("Leveleinstellungen")
				&& playerLineSplit[0].equals("Player")
				&& dimensionsLineSplit.length == 4
				&& settingsLineSplit.length == 4 && playerLineSplit.length == 5) {
			// Dimensionsline verarbeiten
			// FIXME: Entsprechende Variablen erstellen
			// Settingsline verarbeiten
			String[] zoom = settingsLineSplit[1].split(",");
			int zX = 1, zY = 1;
			if (zoom.length == 1) {
				zX = (int) (Float.parseFloat(zoom[0]));
				zY = (int) (Float.parseFloat(zoom[0]));
			} else if (zoom.length == 2) {
				zX = (int) (Float.parseFloat(zoom[0]));
				zY = (int) (Float.parseFloat(zoom[1]));
			} else {
				throw new IOException(
						"Fehler im Aufbau der Leveldatei (Fehler bei Zoom)");
			}
			this.settings.get("Zoom X").setContent("" + zX);
			this.settings.get("Zoom Y").setContent("" + zY);
			// FIXME: Entsprechende Variablen erstellen
			// Playerline verarbeiten
			// FIXME: Entsprechende Variablen erstellen
		} else {
			throw new IOException(
					"Fehler im Aufbau der Leveldatei (In den ersten drei Zeilen)");
		}
		// Alle Objekte löschen
		this.objects.clear();
		// Objekte laden
		int highestID = 0;
		String current = levelFile.readLine();
		while (current != null) {
			System.out.println("Lade Objekt: " + current);
			if (current.split("_").length != 5) {
				throw new IOException(
						"Fehler im Aufbau der Leveldatei (Objektzeile: "
								+ current + " )");
			} else {
				EditorObject cur = new EditorObject(this,
						current.split("_")[2], current.split("_")[0], Vector
								.parseVector(current.split("_")[1]));
				try {
					int id = Integer.parseInt(cur.objectName.split("-")[0]);
					if (id > highestID) {
						highestID = id;
					}
				} catch (NumberFormatException e) {
				}
				this.objects.add(cur);

			}
			current = levelFile.readLine();
		}
		this.curID = highestID;
	}

	@SuppressWarnings("unchecked")
	public void saveLevel(String path) throws IOException {
		FileOutputStream stream = new FileOutputStream(path);
		String firstLine = this.getDimensionsLine();
		String secondLine = "\n" + this.getSettingsLine();
		String thirdLine = "\n" + this.getPlayerLine();
		for (int i = 0; i < firstLine.length(); i++) {
			stream.write((byte) firstLine.charAt(i));
		}
		for (int i = 0; i < secondLine.length(); i++) {
			stream.write((byte) secondLine.charAt(i));
		}
		for (int i = 0; i < thirdLine.length(); i++) {
			stream.write((byte) thirdLine.charAt(i));
		}
		for (EditorObject object : (ArrayList<EditorObject>) this.objects
				.clone()) {
			String line = "\n" + object.getDataLine();
			for (int i = 0; i < line.length(); i++) {
				stream.write((byte) line.charAt(i));
			}
		}
		stream.close();
	}

	private String getPlayerLine() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getSettingsLine() {
		HashMap<String, String> map = this.settings.getContents();
		return "Leveleinstellungen_" + map.get("Zoom X") + ","
				+ map.get("Zoom Y") + "_" + map.get("Zeit") + "_"
				+ map.get("Hintergrund");
	}

	private String getDimensionsLine() {
		HashMap<String, String> map = this.settings.getContents();
		return "Leveldimensionen_" + map.get("Breite") + "_" + map.get("Höhe")
				+ "_" + map.get("Subarea-Breite");
	}
}
