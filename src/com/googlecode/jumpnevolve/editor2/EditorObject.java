/**
 * 
 */
package com.googlecode.jumpnevolve.editor2;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class EditorObject implements Pollable, Drawable {

	/**
	 * 
	 */
	public EditorObject() {
		// TODO Auto-generated constructor stub
	}

	public boolean isPointIn(Vector point) {
		return false;
	}

	@Override
	public void poll(Input input, float secounds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub

	}

	public void drawInterface(Graphics g) {

	}
}
