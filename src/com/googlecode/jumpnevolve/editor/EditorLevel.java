/**
 * 
 */
package com.googlecode.jumpnevolve.editor;

import java.util.ArrayList;

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

	private ArrayList<ObjectSettings> settingsList = new ArrayList<ObjectSettings>();

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
		// Nichts tun
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
		// Spieler zeichnen
		GraphicUtils.draw(g, new Circle(parent.getPlayerPosition(), 30.0f));
	}
}
