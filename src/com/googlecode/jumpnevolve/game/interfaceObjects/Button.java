/**
 * 
 */
package com.googlecode.jumpnevolve.game.interfaceObjects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Interface;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class Button implements Pollable, Drawable {

	private final Interface parent;
	private final Rectangle rect;
	private final int function;
	private final Image icon;

	/**
	 * 
	 */
	public Button(Interface parent, Vector position, Image icon, int function) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		this.rect = new Rectangle(position, new Vector(30.0f, 30.0f));
		this.function = function;
		this.icon = icon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.graphics.Pollable#poll(org.newdawn.slick.Input
	 * , float)
	 */
	@Override
	public void poll(Input input, float secounds) {
		if (input.isMouseButtonDown(0)) {
			if (this.rect.isPointInThis(new Vector(input.getAbsoluteMouseX(),
					input.getAbsoluteMouseY()))) {
				// TODO: AbsoluteMouse oder nur Mouse?
				this.parent.actionPerformed(this.function);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.graphics.Drawable#draw(org.newdawn.slick.Graphics
	 * )
	 */
	@Override
	public void draw(Graphics g) {
		GraphicUtils.drawImage(g, this.rect, this.icon);
	}

}
