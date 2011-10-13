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
 * @author e.wagner
 * 
 */
public class InterfaceLabel extends InterfaceObject {

	private String text;
	private Font curFont;

	/**
	 * @param function
	 */
	public InterfaceLabel(String text, int size) {
		super(InterfaceFunctions.ERROR);
		this.text = text;
		this.curFont = new UnicodeFont(new java.awt.Font("Cambria",
				java.awt.Font.PLAIN, size), size, false, false);
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public Shape getNeededSize() {
		return new Rectangle(Vector.ZERO, this.curFont.getWidth(text),
				this.curFont.getHeight(text));
	}

	@Override
	public void draw(Graphics g) {
		// TODO: Font-Änderung sollte möglich sein
		// this.curFont = g.getFont();
		GraphicUtils.drawString(g, this.parent.getTransformedPositionFor(this),
				this.text);
	}

}
