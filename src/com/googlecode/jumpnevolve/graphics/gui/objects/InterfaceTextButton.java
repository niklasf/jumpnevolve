package com.googlecode.jumpnevolve.graphics.gui.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Button, der aus Text besteht
 * 
 * @author Erik Wagner
 * 
 */
public class InterfaceTextButton extends InterfaceObject {

	/**
	 * size stellt die Texthöhe aller Textbuttons dar.
	 * 
	 * TODO: size hat zur Zeit keine Funktion
	 */
	private static int size = 20;
	private final String buttonText;
	private Rectangle shape = new Rectangle(Vector.ZERO, 1, 1);
	private static Font Font;

	/**
	 * Erzeugt einen TextButton für das Interface
	 * 
	 * @param function
	 *            Die {@link InterfaceFunction}
	 * @param buttonText
	 *            Der Text des Button
	 */
	public InterfaceTextButton(InterfaceFunction function, String buttonText) {
		super(function);
		this.buttonText = buttonText;
	}

	public InterfaceTextButton(InterfaceFunction function, int key,
			String buttonText) {
		super(function, key);
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
	public Rectangle getNeededSize() {
		return this.shape;
	}

	@Override
	public void draw(Graphics g) {
		if (Font == null) {
			Font = g.getFont();
			setTextHeight(Font.getLineHeight());
		}
		if (this.shape.equals(new Rectangle(Vector.ZERO, 1, 1))) {
			int width = Font.getWidth(this.buttonText);
			int height = Font.getHeight(this.buttonText);
			this.shape = new Rectangle(Vector.ZERO, width, height);
		}
		Vector pos = this.getCenterVector();
		this.shape = (Rectangle) this.shape.modifyCenter(pos);
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
		GraphicUtils.drawString(g, this.shape.getHighLeftCorner(),
				this.buttonText);
		g.setColor(c);
	}
}
