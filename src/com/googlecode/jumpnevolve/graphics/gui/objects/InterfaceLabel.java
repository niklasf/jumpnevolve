package com.googlecode.jumpnevolve.graphics.gui.objects;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author e.wagner
 * 
 */
public class InterfaceLabel extends InterfaceObject {

	private String text;

	/**
	 * Die verwendete Schriftart
	 */
	private static Font Font;

	/**
	 * Erzeugt ein neues InterfaceLabel
	 * 
	 * @param text
	 *            Der Text des Labels
	 * @param size
	 *            Die Größe des Textes TODO: size hat zur Zeit keine Funktion
	 * 
	 *            <p>
	 *            TODO: Andere Schriftarten sollten möglich sein
	 *            </p>
	 */
	public InterfaceLabel(String text, int size) {
		super(InterfaceFunctions.INTERFACE_LABEL);
		this.text = text;
	}

	/**
	 * Setzt den Text des Labels neu
	 * 
	 * @param text
	 *            Der neue Text
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public Rectangle getNeededSize() {
		if (Font == null) {
			return new Rectangle(Vector.ZERO, this.text.length() * 10, 12);
		} else {
			return new Rectangle(Vector.ZERO, Font.getWidth(text),
					Font.getHeight(text));
		}
	}

	@Override
	public void draw(Graphics g) {
		if (Font == null) {
			Font = g.getFont();
		}
		GraphicUtils.drawString(g, this.parent.getPositionFor(this), this.text);
	}
}
