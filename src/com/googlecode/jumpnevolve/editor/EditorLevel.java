/**
 * 
 */
package com.googlecode.jumpnevolve.editor;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Level, dass jedoch keine poll-Methoden ausführt, speziell für den Editor
 * 
 * @author Erik Wagner
 * 
 */
public class EditorLevel extends Level {

	private static final int MOVE = 1;
	private static final int PULL_UP = 2;
	private static final int NOT_IDENTIFIED = 0;

	private ArrayList<ObjectSettings> settingsList = new ArrayList<ObjectSettings>();
	private ObjectSettings selected;
	private int selectMode;

	private final Editor parent;

	/**
	 * @param loader
	 * @param width
	 * @param height
	 * @param subareaWidth
	 */
	public EditorLevel(Editor parent) {
		super(new Levelloader(null), 1, 1, 1);
		this.parent = parent;
		// TODO Auto-generated constructor stub
	}

	public void addSettings(ObjectSettings obj) {
		if (settingsList.contains(obj) == false) {
			settingsList.add(obj);
		}
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
		} else {
			this.selected = null;
			this.selectMode = NOT_IDENTIFIED;
		}
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			this.parent.mouseClicked(input.getMouseX(), input.getMouseY());
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
			GraphicUtils
					.string(g, obj.getObjectPosition(), obj.getObjectName());
		}
		// Spieler zeichnen
		GraphicUtils.draw(g, new Circle(parent.getPlayerPosition(), 30.0f));
		GraphicUtils.markPosition(g, parent.getPlayerPosition(), 5);
		GraphicUtils.string(g, parent.getPlayerPosition(), "0000-Player");
	}

	public void clearSettings() {
		this.settingsList.clear();
	}
}
