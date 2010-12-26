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
import com.googlecode.jumpnevolve.math.Circle;

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
		super(new Levelloader(null), 10000, 3000, 100);
		this.parent = parent;
		// TODO Auto-generated constructor stub
	}

	public void add(ObjectSettings obj) {
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
	}

	@Override
	public void draw(Graphics g) {
		this.setZoom(parent.getZoomX(), parent.getZoomY());
		super.configScreen(g);
		this.setBackground(parent.getBackgroundFile());
		super.drawBackground(g);
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
