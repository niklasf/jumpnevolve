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
public class TextButton extends InterfaceObject {

	public final String buttonText;
	public final int size;
	private Rectangle curShape = new Rectangle(Vector.ZERO, 1, 1);

	/**
	 * 
	 * @param function
	 * @param buttonText
	 * @param size
	 */
	public TextButton(int function, String buttonText, int size) {
		super(function);
		this.buttonText = buttonText;
		this.size = size;
	}

	@Override
	public Shape getPreferedSize() {
		return this.curShape;
	}

	@Override
	public void draw(Graphics g) {
		Vector pos = this.getCenterVector();
		pos = pos
				.add(this.parent.getInterfaceable().getCamera().getPosition()
						.sub(
								new Vector(this.parent.getInterfaceable()
										.getWidth() / 2, this.parent
										.getInterfaceable().getHeight() / 2)));
		Font font = g.getFont();
		Font newFont = new UnicodeFont(new java.awt.Font("Cambria",
				java.awt.Font.PLAIN, this.size), this.size, false, false);
		newFont = font;
		/*
		 * FIXME: Die Zeile 55 (newFont = font) soll entfernt werden, zur Zeit
		 * wird dann aber kein Text dargestellt
		 */
		g.setFont(newFont);
		int width = newFont.getWidth(this.buttonText);
		int height = newFont.getHeight(this.buttonText);
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
		g.setFont(font);
	}
}
