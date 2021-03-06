package com.googlecode.jumpnevolve.editor;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.editor.arguments.PositionMarker;
import com.googlecode.jumpnevolve.game.GameObjects;
import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.game.menu.MainMenu;
import com.googlecode.jumpnevolve.graphics.AbstractEngine;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.Interfaceable;
import com.googlecode.jumpnevolve.graphics.gui.MainGUI;
import com.googlecode.jumpnevolve.graphics.gui.container.BorderContainer;
import com.googlecode.jumpnevolve.graphics.gui.container.ButtonList;
import com.googlecode.jumpnevolve.graphics.gui.container.Dialog;
import com.googlecode.jumpnevolve.graphics.gui.container.GridContainer;
import com.googlecode.jumpnevolve.graphics.gui.container.HeadlineContainer;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceButton;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceTextButton;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.JarHandler;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * @author Erik Wagner
 * 
 *         TODO: Level aus dem Userverzeichnis sollten einfach in den Editor zu
 *         laden sein (Checkbox o.Ä.)
 */
public class Editor extends Level implements Interfaceable {

	private static final String DEFAULT_LEVEL = Parameter.EDITOR_EDITOR_DEFAULTLEVEL;
	private static final float OBJECT_DELAY = Parameter.EDITOR_EDITOR_DELAY;

	private ArrayList<EditorObject> objects = new ArrayList<EditorObject>();
	private EditorObject selected;
	private Vector cameraPos = Vector.ZERO, oldCameraPos = Vector.ZERO;
	private int curID;
	private MainGUI gui;
	private boolean guiAction, nextDelete;
	private boolean cameraMove;
	private Vector oldClick;
	private InterfaceFunction lastFunction;
	private Dialog settingsDialog, playerDialog, dataDialog, exitDialog;
	private GridContainer objectSettingsPlace = new GridContainer(1, 1);
	private PositionMarker playerPosition;
	private MainMenu parentMenu;
	private Timer newObjectTimer = new Timer(OBJECT_DELAY);
	private boolean firstRoundPoll = true;
	private boolean toReload = false;

	/**
	 * Erzeugt einen Editor mit einem bestimmten Level als Starteinstellung
	 * 
	 * @param parent
	 *            Das MainMenu, das den Editor geöffnet hat
	 * @param source
	 *            Das Level das als Starteinstellung geladen werden soll
	 * @param width
	 *            Die Breite des Editors
	 * @param height
	 *            Die Höhe des Editors
	 * @param subareaWidth
	 *            Die Subarea-Breite
	 * @throws IOException
	 */
	public Editor(MainMenu parent, String source, int width, int height,
			int subareaWidth) throws IOException {
		super(new Levelloader(source), width, height, subareaWidth);

		this.parentMenu = parent;

		this.playerPosition = new PositionMarker(this, "Player",
				PositionMarker.MODUS_BOTH, Vector.ZERO, Color.red);

		// Settings-Dialog erstellen
		this.settingsDialog = new Dialog();
		this.settingsDialog.addTextField("Name");
		this.settingsDialog.addNumberSelection("Breite", 1, 100000);
		this.settingsDialog.addNumberSelection("Höhe", 1, 100000);
		this.settingsDialog.addTextField("Hintergrund");
		this.settingsDialog.addNumberSelection("Zeit", 1, 10000);
		this.settingsDialog.addNumberSelection("Zoom X", 1, 100);
		this.settingsDialog.addNumberSelection("Zoom Y", 1, 100);
		this.settingsDialog.addNumberSelection("Subarea-Breite", 1, 1000);
		this.settingsDialog.addTextButton(InterfaceFunctions.EDITOR_RELOAD,
				"Einstellungen übernehmen");
		// TODO: Maxima so in Ordnung?

		// Player-Dialog erstellen
		this.playerDialog = new Dialog();
		this.playerDialog.addPart(this.playerPosition.getDialogPart());
		this.playerDialog.addTextField("Startfigur");
		this.playerDialog.addTextField("Verfügbare Figuren");

		// Save-Dialog erstellen
		this.dataDialog = new Dialog();
		this.dataDialog.addTextField("Level laden");
		this.dataDialog.addTextField("Level speichern");
		this.dataDialog.addTextButton(InterfaceFunctions.EDITOR_LOAD, "Laden");
		this.dataDialog.addTextButton(InterfaceFunctions.EDITOR_SAVE,
				"Speichern");

		// Exit-Dialog erstellen
		this.exitDialog = new Dialog();
		this.exitDialog.addTextField("Speichern als");
		this.exitDialog.addTextButton(InterfaceFunctions.EDITOR_SAVE_AND_EXIT,
				"Speichern und beenden");
		this.exitDialog.addTextButton(
				InterfaceFunctions.EDITOR_EXIT_WITHOUT_SAVE,
				"Ohne Speichern beenden");

		// Kopfzeile mit Buttons erstellen
		GridContainer topGrid = new GridContainer(1, 5,
				GridContainer.MODUS_DEFAULT, GridContainer.MODUS_Y_UP);
		topGrid.maximizeXRange();
		topGrid.add(new InterfaceTextButton(InterfaceFunctions.EDITOR_EXIT,
				"Exit"), 0, 0);
		topGrid.add(new InterfaceTextButton(InterfaceFunctions.EDITOR_SETTINGS,
				"Settings"), 0, 1);
		topGrid.add(new InterfaceTextButton(InterfaceFunctions.EDITOR_PLAYER,
				"Player"), 0, 2);
		topGrid.add(new InterfaceTextButton(InterfaceFunctions.EDITOR_CURRENT,
				"Current"), 0, 3);
		topGrid.add(new InterfaceTextButton(InterfaceFunctions.EDITOR_DATA,
				"Data"), 0, 4);
		topGrid.enableBackground();

		// Löschen-Button erstellen
		InterfaceButton deleteButton = new InterfaceButton(
				InterfaceFunctions.EDITOR_DELETE, "interface-icons/delete.png");

		// Auswahlliste für die GameObjects erstellen
		ButtonList selectList = new ButtonList(6, 10);
		for (GameObjects obj : GameObjects.values()) {
			selectList.addButton(new InterfaceButton(obj,
					obj.editorSkinFileName));
		}

		// BorderContainer für die Oberfläche erstellen und befüllen
		BorderContainer border = new BorderContainer();
		border.maximizeSize();

		border.add(selectList, BorderContainer.POSITION_LOW_LEFT);
		border.add(this.settingsDialog, BorderContainer.POSITION_MIDDLE);
		border.add(this.playerDialog, BorderContainer.POSITION_MIDDLE);
		border.add(this.objectSettingsPlace, BorderContainer.POSITION_MIDDLE);
		border.add(this.dataDialog, BorderContainer.POSITION_MIDDLE);
		border.add(this.exitDialog, BorderContainer.POSITION_MIDDLE);
		border.add(deleteButton, BorderContainer.POSITION_LOW_RIGHT);

		// HeadlineContainer als übergeordneten Container erstellen, beinhaltet
		// die Kopfzeile und borderContainer
		HeadlineContainer headCon = new HeadlineContainer(topGrid, border);

		// MainGUI erstellen, MainContainer ist der HeadlineContainer
		this.gui = new MainGUI(this);
		this.gui.setMainContainer(headCon);

		// Kamera setzen
		this.setCamera(new EditorCamera(this));
		this.setCameraPosition(new Vector(this.getWidth(), this.getHeight())
				.div(2.0f));

		// Zoom auf 1 setzen, damit das Interface richtig dargestellt wird
		this.setZoom(1);

		// Default-Level laden
		this.loadLevel(source);
	}

	/**
	 * Erzeugt einen Editor mit dem Default-Level als Starteinstellung
	 * 
	 * @param width
	 *            Die Breite des Editors
	 * @param height
	 *            Die Höhe des Editors
	 * @param subareaWidth
	 *            Die Subarea-Breite
	 * @throws IOException
	 */
	public Editor(int width, int height, int subareaWidth) throws IOException {
		this(null, DEFAULT_LEVEL, width, height, subareaWidth);
	}

	public Editor(MainMenu parent, int width, int height, int subareaWidth)
			throws IOException {
		this(parent, DEFAULT_LEVEL, width, height, subareaWidth);
	}

	public Editor(String source, int width, int height, int subareaWidth)
			throws IOException {
		this(null, source, width, height, subareaWidth);
	}

	private void addNewObject(GameObjects function, Vector position) {
		String className = function.getClassNameForEditor();
		EditorObject obj = new EditorObject(this,
				this.getObjectName(className), className, position);
		this.addNewObject(obj, function);
	}

	private void addNewObject(EditorObject obj, GameObjects function) {
		function.args.initObject(obj);
		this.objects.add(obj);
		this.objectSettingsPlace.add(obj.settings, 0, 0);
		this.selected = obj;
	}

	private String getObjectName(String className) {
		this.curID++;
		return this.getTransformedId(this.curID) + "-" + className;
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

	public float getCameraPosY() {
		return cameraPos.y;
	}

	public float getCameraPosX() {
		return cameraPos.x;
	}

	public Vector getCameraPos() {
		return this.cameraPos;
	}

	private void setCameraPosition(Vector newPos) {
		this.cameraPos = newPos;
	}

	private void exit() {
		// Nachfragen, ob das Programm wirklich beendet werden soll
		// Bei "Ja" Editor beenden
		if (this.parentMenu != null) {
			Log.info("Exit the Editor");
			Dialog.disableAll();
			this.parentMenu.switchBackToMainState();
			Engine.getInstance().switchState(this.parentMenu);
		} else {
			System.exit(0);
		}
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
		// Alle Objekte einmal pollen, wenn das Level gerade geladen wurde
		if (this.firstRoundPoll) {
			for (EditorObject obj : this.objects) {
				obj.poll(input, secounds);
			}
			this.firstRoundPoll = false;
		}
		// Editor neu laden, wenn gerade ein Level manuell geladen wurde
		if (this.toReload) {
			try {
				this.loadWithNewSettings();
			} catch (IOException e) {
				// TODO Fehlermeldung im Editor ausgeben
				e.printStackTrace();
			}
		}
		this.guiAction = false;
		this.gui.poll(input, secounds);
		if (this.selected != null) {
			this.selected.poll(input, secounds);
		}
		if (!this.guiAction) {
			this.guiAction = PositionMarker.isAnyMarkerSelected();
		}
		if (!Dialog.isAnyDialogActive()) {
			Vector mousePos = new Vector(input.getMouseX(), input.getMouseY());
			Vector translatedMousePos = this.translateMousePos(mousePos);
			if (!guiAction) {
				if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
						&& this.lastFunction instanceof GameObjects
						&& !this.newObjectTimer.isRunning()) {
					this.addNewObject((GameObjects) this.lastFunction,
							translatedMousePos);
					this.newObjectTimer.start(OBJECT_DELAY);
					this.lastFunction = null;
				}
			}
			if (this.selected == null) {
				this.playerPosition.poll(input, secounds);
				if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
						&& !this.playerPosition.isMoving()) {
					if (!this.guiAction) {
						// Kamera verschieben
						if (!this.cameraMove) {
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
				} else {
					this.cameraMove = false;
					this.oldCameraPos = this.getCameraPos();
				}
			}
			if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
				EditorObject object = null;
				boolean found = false;
				for (EditorObject obj : (ArrayList<EditorObject>) this.objects
						.clone()) {
					if (obj.isPointIn(translatedMousePos)) {
						object = obj;
						found = true;
					}
				}
				if (!found) {
					object = null;
				}
				if (this.nextDelete) {
					this.deleteObject(object);
				} else {
					this.selected = object;
				}
			} else if (input.isKeyPressed(Input.KEY_DELETE)) {
				this.deleteObject(this.selected);
			}
		}
		this.newObjectTimer.poll(input, secounds);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		for (EditorObject obj : (ArrayList<EditorObject>) this.objects.clone()) {
			obj.draw(g);
		}
		if (this.selected != null) {
			this.selected.drawInterface(g);
		}
		this.drawPlayer(g);
		this.gui.draw(g);
	}

	private void drawPlayer(Graphics g) {
		GraphicUtils.fill(g, new Circle(this.playerPosition.getPosition(),
				10.0f), Color.blue);
		this.playerPosition.draw(g);
		GraphicUtils.drawString(g, this.playerPosition.getPosition(), "Player");
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
		if (!Dialog.isAnyDialogActive()) {
			// Aktionen, wenn kein Dialog aktiv ist
			if (function instanceof GameObjects) {
			} else if (function == InterfaceFunctions.EDITOR_DELETE) {
				this.nextDelete = true;
			} else {
				if (function == InterfaceFunctions.EDITOR_SETTINGS) {
					this.settingsDialog.show();
				}
				if (function == InterfaceFunctions.EDITOR_PLAYER) {
					this.playerDialog.show();
				}
				if (function == InterfaceFunctions.EDITOR_CURRENT) {
					if (this.selected != null) {
						this.selected.showDialog();
					}
				}
				if (function == InterfaceFunctions.EDITOR_DATA) {
					this.dataDialog.show();
				}
				if (function == InterfaceFunctions.EDITOR_EXIT) {
					this.exitDialog.show();
				}
			}
		} else {
			// Aktionen aus den Dialogen
			if (function == InterfaceFunctions.EDITOR_SAVE) {
				try {
					this.saveLevel(this.dataDialog.getContentable(
							"Level speichern").getContent());
					this.dataDialog.hide();
				} catch (IOException e) {
					// TODO Fehlermeldung im Editor ausgeben
					e.printStackTrace();
				}
			}
			if (function == InterfaceFunctions.EDITOR_LOAD) {
				try {
					this.loadLevel(this.dataDialog
							.getContentable("Level laden").getContent());
					this.toReload = true;
				} catch (IOException e) {
					// TODO Fehlermeldung im Editor ausgeben
					e.printStackTrace();
				}
			}
			if (function == InterfaceFunctions.EDITOR_RELOAD) {
				try {
					this.loadWithNewSettings();
				} catch (IOException e) {
					// TODO Fehlermeldung im Editor ausgeben
					e.printStackTrace();
				}
			}
			if (function == InterfaceFunctions.EDITOR_SAVE_AND_EXIT) {
				try {
					this.saveLevel(this.exitDialog.getContentable(
							"Speichern als").getContent());
					this.dataDialog.hide();
					this.exit();
				} catch (IOException e) {
					// TODO Fehlermeldung im Editor ausgeben
					e.printStackTrace();
				}
			}
			if (function == InterfaceFunctions.EDITOR_EXIT_WITHOUT_SAVE) {
				this.exit();
			}
		}
		this.lastFunction = function;
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		this.guiAction = true;
	}

	@Override
	public void objectIsSelected(InterfaceObject object) {
		// Nichts tun
	}

	public void loadLevel(String path) throws IOException {
		path = this.transformToInputPath(path);

		Log.info("Versuche Level in Editor zu laden: " + path);

		BufferedReader levelFile;
		if (JarHandler.existJar()) {
			levelFile = new BufferedReader(new InputStreamReader(this
					.getClass().getResourceAsStream(path)));
		} else {
			levelFile = new BufferedReader(new FileReader(path));
		}

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
				&& settingsLineSplit.length == 4 && playerLineSplit.length == 4) {

			// Dimensionsline verarbeiten
			this.settingsDialog.getContentable("Breite").setContent(
					dimensionsLineSplit[1]);
			this.settingsDialog.getContentable("Höhe").setContent(
					dimensionsLineSplit[2]);
			this.settingsDialog.getContentable("Subarea-Breite").setContent(
					dimensionsLineSplit[3]);

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
			this.settingsDialog.getContentable("Zoom X").setContent("" + zX);
			this.settingsDialog.getContentable("Zoom Y").setContent("" + zY);
			this.settingsDialog.getContentable("Zeit").setContent(
					settingsLineSplit[2]);
			this.settingsDialog.getContentable("Hintergrund").setContent(
					settingsLineSplit[3]);

			this.setBackground(settingsLineSplit[3]);

			// Playerline verarbeiten
			this.playerDialog.getContentable("Startfigur").setContent(
					playerLineSplit[1]);
			this.playerDialog.getContentable("Verfügbare Figuren").setContent(
					playerLineSplit[2]);
			this.playerPosition.initialize(playerLineSplit[3]);

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

			Log.info("Lade Objekt: " + current);

			if (current.split("_").length != 5) {
				throw new IOException(
						"Fehler im Aufbau der Leveldatei (Objektzeile: "
								+ current + " )");
			} else {
				String[] currentSplit = current.split("_");
				EditorObject cur = new EditorObject(this, currentSplit[2],
						currentSplit[0], Vector.parseVector(currentSplit[1]));
				try {
					int id = Integer.parseInt(cur.objectName.split("-")[0]);
					if (id > highestID) {
						highestID = id;
					}
				} catch (NumberFormatException e) {
				}
				this.addNewObject(cur,
						GameObjects.getGameObject(currentSplit[0]));
				cur.initialize(currentSplit[3], currentSplit[4]);
			}
			current = levelFile.readLine();
		}
		this.curID = highestID;

		this.firstRoundPoll = true;

		Log.info("Level erfolgreich geladen");
	}

	@SuppressWarnings("unchecked")
	public void saveLevel(String path) throws IOException {
		path = this.transformToOutputPath(path);

		Log.info("Versuche Level zu speichern: " + path);

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

		Log.info("Speichern erfolgreich beendet");
	}

	private void deleteObject(EditorObject object) {
		if (object != null) {
			Log.info("Lösche Objekt: " + object.getDataLine());
			object.hideDialog();
			this.objects.remove(object);
			this.selected = null;
			this.nextDelete = false;
		}
	}

	private void loadWithNewSettings() throws IOException {
		Log.info("Übernehme Einstellungen für den Editor");
		Dialog.disableAll();
		this.saveLevel("reload-save.txt");
		AbstractEngine engine = Engine.getInstance();
		engine.switchState(new Editor(this.parentMenu, "reload-save.txt",
				Integer.parseInt(this.settingsDialog.getContentable("Breite")
						.getContent()), Integer.parseInt(this.settingsDialog
						.getContentable("Höhe").getContent()), Integer
						.parseInt(this.settingsDialog.getContentable(
								"Subarea-Breite").getContent())));
	}

	/**
	 * Formt den String so um, dass das Level in "resources/levels/" abgelegt
	 * wird und die Endung ".txt" hat
	 * 
	 * Pfad-Angaben werden ignoriert, Inhalt hinter "." wird entfernt und durch
	 * ".txt" ersetzt
	 * 
	 * @param content
	 *            Der String, der umgeformt werden soll
	 * @return Der umgeformte String
	 */
	private String transformToInputPath(String content) {
		if (content.startsWith(Parameter.PROGRAMM_DIRECTORY_LEVELS)) {
			return content;
		}
		if (JarHandler.existJar()) {
			if (content.lastIndexOf("/") != -1) {
				return "/levels" + content.substring(content.lastIndexOf("/"));
			} else {
				return "/levels/" + content;
			}
		} else {
			int lastPoint = content.indexOf(".");
			if (lastPoint != -1) {
				content = content.substring(0, lastPoint);
			}
			content += ".txt";
			String[] parts = content.split("/");
			switch (parts.length) {
			case 1:
				return "resources/levels/" + content;
			default:
				return "resources/levels/" + parts[parts.length - 1];
			}
		}
	}

	/**
	 * Formt den String so um, dass das Level in "resources/levels/" abgelegt
	 * wird und die Endung ".txt" hat
	 * 
	 * Pfad-Angaben werden ignoriert, Inhalt hinter "." wird entfernt und durch
	 * ".txt" ersetzt
	 * 
	 * @param content
	 *            Der String, der umgeformt werden soll
	 * @return Der umgeformte String
	 */
	private String transformToOutputPath(String content) {
		if (JarHandler.existJar()) {
			return Parameter.PROGRAMM_DIRECTORY_LEVELS
					+ content.split("/")[content.split("/").length - 1];
		} else {
			int lastPoint = content.indexOf(".");
			if (lastPoint != -1) {
				content = content.substring(0, lastPoint);
			}
			content += ".txt";
			String[] parts = content.split("/");
			switch (parts.length) {
			case 1:
				return "resources/levels/" + content;
			default:
				return "resources/levels/" + parts[parts.length - 1];
			}
		}
	}

	private String getPlayerLine() {
		HashMap<String, String> map = this.playerDialog.getContents();
		return "Player_" + map.get("Startfigur") + "_"
				+ map.get("Verfügbare Figuren") + "_"
				+ this.playerPosition.getArgumentPart();
	}

	private String getSettingsLine() {
		HashMap<String, String> map = this.settingsDialog.getContents();
		return "Leveleinstellungen_" + map.get("Zoom X") + ","
				+ map.get("Zoom Y") + "_" + map.get("Zeit") + "_"
				+ map.get("Hintergrund");
	}

	private String getDimensionsLine() {
		HashMap<String, String> map = this.settingsDialog.getContents();
		return "Leveldimensionen_" + map.get("Breite") + "_" + map.get("Höhe")
				+ "_" + map.get("Subarea-Breite");
	}
}
