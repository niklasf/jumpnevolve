/**
 * 
 */
package com.googlecode.jumpnevolve.editor;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.gui.BorderContainer;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceButton;
import com.googlecode.jumpnevolve.graphics.gui.ButtonList;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.Interfaceable;
import com.googlecode.jumpnevolve.graphics.gui.MainGUI;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Level, das in seiner poll-Methode die Ereignisverarbeitung übernimmt,
 * speziell für den Editor
 * 
 * @author Erik Wagner
 * 
 */
public class EditorLevel extends Level implements Interfaceable {

	private static final int ACTION_MOVE = 1;
	private static final int ACTION_PULL_UP = 2;
	private static final int ACTION_CAMERA = 3;
	private static final int ACTION_INTERFACE_MOUSE_CLICKED = 4;
	private static final int ACTION_INTERFACE_MOUSE_OVER = 5;
	private static final int ACTION_NOT_IDENTIFIED = 0;

	private ArrayList<ObjectSettings> settingsList = new ArrayList<ObjectSettings>();
	private ObjectSettings selected;
	private int selectedActionMode = ACTION_NOT_IDENTIFIED,
			oldSelectedActionMode = ACTION_NOT_IDENTIFIED;
	private Vector oldCameraPos, oldClick;
	private InterfaceFunctions lastInterfaceFunction = InterfaceFunctions.ERROR;
	private boolean interfaceActionThisRound = false,
			modeAlreadyChanged = false;

	private final Editor parent;

	private final MainGUI gui;

	/**
	 * @param loader
	 * @param width
	 * @param height
	 * @param subareaWidth
	 */
	public EditorLevel(Editor parent) {
		super(new Levelloader(null), 1, 1, 1);
		this.parent = parent;
		this.gui = new MainGUI(this);
		ButtonList selectList = new ButtonList(6, 10);
		BorderContainer border = new BorderContainer();
		border.add(selectList, BorderContainer.POSITION_LOW_LEFT);
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.EDITOR_GROUND, "textures/stone.png"));
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.EDITOR_DOOR, "textures/wood.png"));
		selectList
				.addButton(new InterfaceButton(
						InterfaceFunctions.EDITOR_BUTTON,
						"textures/aluminium.png", 'B'));
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.EDITOR_ELEVATOR, "textures/aluminium.png",
				'E'));
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.EDITOR_SLIDING_PLATTFORM,
				"textures/aluminium.png", 'S'));
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.EDITOR_SOLDIER,
				"object-pictures/simple-foot-soldier.png", 'S'));
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.EDITOR_WALKING_SOLDIER,
				"object-pictures/simple-foot-soldier.png", 'W'));
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.EDITOR_JUMPING_SOLDIER,
				"object-pictures/simple-foot-soldier.png", 'J'));
		selectList.addButton(new InterfaceButton(
				InterfaceFunctions.EDITOR_GREEN_SLIME_WORM,
				"object-pictures/green-slime-worm.png"));
		gui.setMainContainer(border);
	}

	private void setSelectedActionMode(int newMode) {
		if (modeAlreadyChanged == false) {
			this.oldSelectedActionMode = this.selectedActionMode;
			this.modeAlreadyChanged = true;
		}
		this.selectedActionMode = newMode;
	}

	public void addSettings(ObjectSettings obj) {
		if (settingsList.contains(obj) == false) {
			settingsList.add(obj);
		}
	}

	public void addSettingsButton(String className, Image icon) {
		// Buttons erstellen, die es dem Benutzer ermöglichen, direkt in der
		// Engine Objekte zu erstellen
	}

	public void remove(ObjectSettings obj) {
		settingsList.remove(obj);
	}

	@Override
	public void poll(Input input, float secounds) {
		this.modeAlreadyChanged = false;
		this.interfaceActionThisRound = false;
		this.gui.poll(input, secounds);
		// Mausereignis-Verarbeitung

		// Anwendung des letzten gewählten Modus
		if (this.selected != null
				&& this.selectedActionMode != ACTION_NOT_IDENTIFIED) {
			if (this.selectedActionMode == ACTION_MOVE) {
				Vector mousePos = this.parent.translateMouseClick(input
						.getMouseX(), input.getMouseY());
				this.selected.setPosition(mousePos.x, mousePos.y);
			} else if (this.selectedActionMode == ACTION_PULL_UP) {
				Vector mousePos = this.parent.translateMouseClick(input
						.getMouseX(), input.getMouseY());
				Vector pos = this.selected.getObjectPosition();
				Vector vec = mousePos.sub(pos);
				if (vec.x == 0) {
					vec = vec.modifyX(1);
				}
				if (vec.y == 0) {
					vec = vec.modifyY(1);
				}
				vec = vec.modifyX(Math.abs(vec.x));
				vec = vec.modifyY(Math.abs(vec.y));
				this.selected.setDimension(vec.x, vec.y);
			}
		}
		if (this.selectedActionMode == ACTION_CAMERA) {
			Vector MousePos = new Vector(input.getMouseX(), input.getMouseY());
			if (this.oldClick == null) {
				this.oldClick = MousePos;
			}
			Vector dif = MousePos.sub(oldClick);
			dif = dif.modifyX(dif.x / this.parent.getZoomX());
			dif = dif.modifyY(dif.y / this.parent.getZoomY());
			this.parent.setCameraPosition(this.oldCameraPos.sub(dif));
		}

		// Prüfen einer neuen Interaktion
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			if (this.interfaceActionThisRound == false) {
				ArrayList<ObjectSettings> abbild = settingsList;
				if (this.selected != null
						&& this.selectedActionMode != ACTION_NOT_IDENTIFIED) {
					if (this.selectedActionMode == ACTION_MOVE) {
						if (this.selected.isMoveSelectedWithMouse(input
								.getMouseX(), input.getMouseY(), false) == false) {
							this.selected = null;
							this.setSelectedActionMode(ACTION_NOT_IDENTIFIED);
						} else {
							this.setSelectedActionMode(ACTION_MOVE);
						}
					} else if (this.selectedActionMode == ACTION_PULL_UP) {
						if (this.selected.isPullUpSelectedWithMouse(input
								.getMouseX(), input.getMouseY(), false) == false) {
							this.selected = null;
							this.setSelectedActionMode(ACTION_NOT_IDENTIFIED);
						} else {
							this.setSelectedActionMode(ACTION_PULL_UP);
						}
					}
				} else {
					this.selected = null;
					this.setSelectedActionMode(ACTION_NOT_IDENTIFIED);
				}
				if (this.selected == null) {
					for (ObjectSettings obj : abbild) {
						if (obj.isMoveSelectedWithMouse(input.getMouseX(),
								input.getMouseY(), false)) {
							if (this.selected == null) {
								this.selected = obj;
								this.setSelectedActionMode(ACTION_MOVE);
							} else {
								this
										.setSelectedActionMode(ACTION_NOT_IDENTIFIED);
							}
						}
						if (obj.isPullUpSelectedWithMouse(input.getMouseX(),
								input.getMouseY(), false)) {
							if (this.selected == null) {
								this.selected = obj;
								this.setSelectedActionMode(ACTION_PULL_UP);
							} else {
								this
										.setSelectedActionMode(ACTION_NOT_IDENTIFIED);
							}
						}
					}
				}
				if (this.selected == null) {
					for (ObjectSettings obj : abbild) {
						if (obj.getObject().getShape().isPointInThis(
								this.parent.translateMouseClick(input
										.getMouseX(), input.getMouseY()))) {
							if (this.selected == null) {
								this.selected = obj;
								this
										.setSelectedActionMode(ACTION_NOT_IDENTIFIED);
							} else {
								this
										.setSelectedActionMode(ACTION_NOT_IDENTIFIED);
							}
						}
					}
				}
				if (this.selected == null) {
					this.setSelectedActionMode(ACTION_CAMERA);
				}
			}
		} else {
			this.selected = null;
			this.setSelectedActionMode(ACTION_NOT_IDENTIFIED);
			this.oldCameraPos = this.getCamera().getPosition();
			this.oldClick = null;
		}

		// Objekt erstellen, wenn letzte Runde das Interface angeklickt wurde
		// und diese sich der Mauszeiger nicht mehr auf dem Interface befindet
		// und die linke Maustaste weiterhin gedrückt ist
		if (this.oldSelectedActionMode == ACTION_INTERFACE_MOUSE_CLICKED
				&& this.interfaceActionThisRound == false
				&& input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			String className = this.lastInterfaceFunction
					.getClassNameForEditor();
			if (className != null) {
				this.parent.addNewObject(className, this.parent
						.translateMouseClick(new Vector(input.getMouseX(),
								input.getMouseY())));
			}
			// Aktion ist gleich MOVE
			this.setSelectedActionMode(ACTION_MOVE);
			this.selected = this.parent.getCurrentSettings();
		}

		// Linker Mausklick an den Editor weitergeben
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			this.parent.mouseClicked(input.getMouseX(), input.getMouseY());
		}
		// Mit rechtem Mausklick ein Objekt auswählen
		if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			Vector pos = this.parent.translateMouseClick(input.getMouseX(),
					input.getMouseY());
			ArrayList<ObjectSettings> abbild = settingsList;
			ObjectSettings select = null;
			boolean onlyOne = false;
			for (ObjectSettings obj : abbild) {
				if (obj.getObject().getShape().isPointInThis(pos)) {
					if (select == null) {
						select = obj;
						onlyOne = true;
					} else {
						onlyOne = false;
					}
				}
			}
			if (select != null && onlyOne == true) {
				this.parent.setCurrentSettings(select);
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		this.setZoom(parent.getZoomX(), parent.getZoomY());
		super.configScreen(g);
		this.setBackground(parent.getBackgroundFile());
		// super.drawBackground(g);
		// Hintergrund für das spätere Level malen
		GraphicUtils.drawImage(g, new Rectangle(new Vector(
				parent.getCurWidth() / 2.0f, parent.getCurHeight() / 2.0f),
				parent.getCurWidth(), parent.getCurHeight()), ResourceManager
				.getInstance().getImage(this.getBackgroundFile()));
		// Objekte darstellen, es wird abbild benutzt, damit in der for-Schleife
		// keine Probleme auftreten
		ArrayList<ObjectSettings> abbild = settingsList;
		for (ObjectSettings obj : abbild) {
			obj.getObject().draw(g);
		}
		for (ObjectSettings obj : abbild) {
			// GraphicUtils.markPosition(g, obj.getObjectPosition(), 5);
			Color c = g.getColor();
			g.setColor(Color.red);
			GraphicUtils.draw(g, new Circle(obj.getObjectPosition(),
					ObjectSettings.SELECT_DISTANCE));
			g.setColor(Color.green);
			for (Vector vec : obj.getPullUpPositions()) {
				GraphicUtils.draw(g, new Circle(vec,
						ObjectSettings.SELECT_DISTANCE));
			}
			g.setColor(c);
			GraphicUtils.drawString(g, obj.getObjectPosition(), obj
					.getObjectName());
		}
		// Spieler zeichnen
		GraphicUtils.draw(g, new Circle(parent.getPlayerPosition(), 30.0f));
		GraphicUtils.markPosition(g, parent.getPlayerPosition(), 5);
		GraphicUtils.drawString(g, parent.getPlayerPosition(), "0000-Player");
		this.gui.draw(g);
	}

	public void clearSettings() {
		this.settingsList.clear();
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
		this.setSelectedActionMode(ACTION_INTERFACE_MOUSE_CLICKED);
		if (object.getFunction().getKindOfParent().equals("EDITOR")) {
			this.lastInterfaceFunction = object.getFunction();
		} else {
			this.lastInterfaceFunction = InterfaceFunctions.ERROR;
		}
		this.interfaceActionThisRound = true;
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		this.interfaceActionThisRound = true;
		this.setSelectedActionMode(ACTION_INTERFACE_MOUSE_OVER);
	}
}
