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
import com.googlecode.jumpnevolve.graphics.gui.Button;
import com.googlecode.jumpnevolve.graphics.gui.ButtonList;
import com.googlecode.jumpnevolve.graphics.gui.GridContainer;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceConstants;
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

	private static final int MOVE = 1;
	private static final int PULL_UP = 2;
	private static final int CAMERA = 3;
	private static final int NOT_IDENTIFIED = 0;

	private ArrayList<ObjectSettings> settingsList = new ArrayList<ObjectSettings>();
	private ObjectSettings selected;
	private int selectMode = NOT_IDENTIFIED;
	private Vector oldCameraPos, oldClick;

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
		GridContainer grid = new GridContainer(this.gui, 3, 3,
				GridContainer.MODUS_X_LEFT, GridContainer.MODUS_DEFAULT);
		ButtonList selectList = new ButtonList(grid, 3, 10);
		grid.add(selectList, 2, 0);
		selectList.addButton(new Button(selectList,
				InterfaceConstants.EDITOR_GROUND, "textures/stone.png"));
		selectList.addButton(new Button(selectList,
				InterfaceConstants.EDITOR_SOLDIER,
				"object-pictures/simple-foot-soldier.png"));
		selectList.addButton(new Button(selectList,
				InterfaceConstants.EDITOR_BUTTON, "textures/aluminium.png"));
		selectList.addButton(new Button(selectList,
				InterfaceConstants.EDITOR_DOOR, "textures/wood.png"));
		gui.setMainContainer(grid);
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
		// Mausereignis-Verarbeitung
		if (this.selected != null && this.selectMode != NOT_IDENTIFIED) {
			if (this.selectMode == MOVE) {
				Vector mousePos = this.parent.translateMouseClick(input
						.getMouseX(), input.getMouseY());
				this.selected.setPosition(mousePos.x, mousePos.y);
			} else if (this.selectMode == PULL_UP) {
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
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			ArrayList<ObjectSettings> abbild = settingsList;
			if (this.selected != null && this.selectMode != NOT_IDENTIFIED) {
				if (this.selectMode == MOVE) {
					if (this.selected.isMoveSelectedWithMouse(
							input.getMouseX(), input.getMouseY(), false) == false) {
						this.selected = null;
						this.selectMode = NOT_IDENTIFIED;
					}
				} else if (this.selectMode == PULL_UP) {
					if (this.selected.isPullUpSelectedWithMouse(input
							.getMouseX(), input.getMouseY(), false) == false) {
						this.selected = null;
						this.selectMode = NOT_IDENTIFIED;
					}
				}
			} else {
				this.selected = null;
				this.selectMode = NOT_IDENTIFIED;
			}
			if (this.selected == null) {
				for (ObjectSettings obj : abbild) {
					if (obj.isMoveSelectedWithMouse(input.getMouseX(), input
							.getMouseY(), false)) {
						if (this.selected == null) {
							this.selected = obj;
							this.selectMode = MOVE;
						} else {
							this.selectMode = NOT_IDENTIFIED;
						}
					}
					if (obj.isPullUpSelectedWithMouse(input.getMouseX(), input
							.getMouseY(), false)) {
						if (this.selected == null) {
							this.selected = obj;
							this.selectMode = PULL_UP;
						} else {
							this.selectMode = NOT_IDENTIFIED;
						}
					}
				}
			}
			if (this.selected == null) {
				for (ObjectSettings obj : abbild) {
					if (obj.getObject().getShape().isPointInThis(
							this.parent.translateMouseClick(input.getMouseX(),
									input.getMouseY()))) {
						if (this.selected == null) {
							this.selected = obj;
							this.selectMode = NOT_IDENTIFIED;
						} else {
							this.selectMode = NOT_IDENTIFIED;
						}
					}
				}
			}
			if (this.selected == null) {
				Vector MousePos = new Vector(input.getMouseX(), input
						.getMouseY());
				if (this.oldClick == null) {
					this.oldClick = MousePos;
				}
				this.selectMode = CAMERA;
				Vector dif = MousePos.sub(oldClick);
				dif = dif.modifyX(dif.x / this.parent.getZoomX());
				dif = dif.modifyY(dif.y / this.parent.getZoomY());
				this.parent.setCameraPosition(this.oldCameraPos.sub(dif));
			}
		} else {
			this.selected = null;
			this.selectMode = NOT_IDENTIFIED;
			this.oldCameraPos = this.getCamera().getPosition();
			this.oldClick = null;
		}
		// TODO: selectMode = CAMERA einbauen
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			this.parent.mouseClicked(input.getMouseX(), input.getMouseY());
		}
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
		this.gui.poll(input, secounds);
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
			GraphicUtils
					.string(g, obj.getObjectPosition(), obj.getObjectName());
		}
		// Spieler zeichnen
		GraphicUtils.draw(g, new Circle(parent.getPlayerPosition(), 30.0f));
		GraphicUtils.markPosition(g, parent.getPlayerPosition(), 5);
		GraphicUtils.string(g, parent.getPlayerPosition(), "0000-Player");
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
	public void interfaceAction(int function, InterfaceObject object) {
		// TODO Auto-generated method stub
		System.out.println("Action: " + function);
	}
}
