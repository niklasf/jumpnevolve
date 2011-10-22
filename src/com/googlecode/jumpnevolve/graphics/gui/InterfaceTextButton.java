package com.googlecode.jumpnevolve.graphics.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Button, der aus Text besteht
 * 
 * @author Erik Wagner
 * 
 */
public class InterfaceTextButton extends InterfaceObject {

	private static int size = 20;
	private final String buttonText;
	private Rectangle curShape = new Rectangle(Vector.ZERO, 1, 1);
	private static Font Font;

	/**
	 * 
	 * @param function
	 * @param buttonText
	 * @param size
	 */
	public InterfaceTextButton(InterfaceFunction function, String buttonText) {
		super(function);
		this.buttonText = buttonText;
	}

	public static void setTextHeight(int newHeight) {
		if (newHeight > 0) {
			size = newHeight;
		}
	}

	public static int getSize() {
		return size;
	}

	public String getText() {
		return this.buttonText;
	}

	@Override
	public Shape getNeededSize() {
		return this.curShape;
	}

	@Override
	public void draw(Graphics g) {
		if (Font == null) {
			Font = g.getFont();
		}
		Vector pos = this.getTransformedCenterVector();
		int width = Font.getWidth(this.buttonText);
		int height = Font.getHeight(this.buttonText);
		this.curShape = new Rectangle(pos, width, height);
		Color c = g.getColor();
		switch (this.getStatus()) {
		case STATUS_MOUSE_OVER:
			g.setColor(Color.yellow);
			break;
		case STATUS_DOWN:
		case STATUS_PRESSED:
			g.setColor(Color.cyan);
			break;
		case STATUS_NOTHING:
		default:
			g.setColor(Color.white);
			break;
		}
		GraphicUtils.drawString(g, this.curShape.getHighLeftCorner(),
				this.buttonText);
		g.setColor(c);
	}
}
