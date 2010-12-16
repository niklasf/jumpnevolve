package com.googlecode.jumpnevolve.game;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.interfaceObjects.Button;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class Interface implements Pollable, Drawable {

	private ArrayList<Button> list = new ArrayList<Button>();
	private final Player parent;

	/**
	 * 
	 */
	public Interface(Player level) {
		this.parent = level;
		// TODO: Buttons erzeugen
	}

	public void addFunction(Vector position, int function) {
		Image icon = getIcon(function);
		this.list.add(new Button(this, position, icon, function));
	}

	private Image getIcon(int function) {
		// FIXME: Bilddateinamen austauschen / Dateien erstellen
		switch (function) {
		case Player.ROLLING_BALL:
			return ResourceManager.getInstance().getImage(
					"Rolling-Ball-Icon.png");
		case Player.JUMPING_CROSS:
			return ResourceManager.getInstance().getImage(
					"Jumping-Cross-Icon.png");
		case Player.HIGH_JUMP:
			return ResourceManager.getInstance().getImage("High-Jump-Icon.png");
		default:
			return null; // FIXME: Fehlermeldung ausgeben
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		for (Button but : list) {
			but.poll(input, secounds);
		}
	}

	@Override
	public void draw(Graphics g) {
		for (Button but : list) {
			but.draw(g);
		}
	}

	public void actionPerformed(int function) {
		switch (function) {
		case Player.ROLLING_BALL:
			this.parent.changeFigure(Player.ROLLING_BALL);
			break;
		case Player.JUMPING_CROSS:
			this.parent.changeFigure(Player.JUMPING_CROSS);
			break;
		case Player.HIGH_JUMP:
			this.parent.activateSkill(Player.HIGH_JUMP);
			break;
		default:
			break;
		}
	}
}
