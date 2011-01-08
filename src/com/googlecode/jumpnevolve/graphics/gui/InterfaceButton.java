/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class InterfaceButton extends InterfaceObject {

	public static final float BUTTON_DIMENSION = 50.0f;

	private final String icon;
	private char name = ' ';
	private final Shape shape;

	/**
	 * Erzeigt einen neuen Button für das Interface
	 * 
	 * @param function
	 *            Die Funktion des Buttons (Ein Enum aus
	 *            {@link InterfaceFunctions});
	 * @param icon
	 *            Der Datei-Pfad des Icons, welches auf diesem Button
	 *            dargestellt werden soll
	 */
	public InterfaceButton(InterfaceFunctions function, String iconPath) {
		super(function);
		this.icon = iconPath;
		this.shape = new Rectangle(Vector.ZERO, BUTTON_DIMENSION,
				BUTTON_DIMENSION);
	}

	public InterfaceButton(InterfaceFunctions function, String iconPath,
			char name) {
		this(function, iconPath);
		this.name = name;
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
		Shape actShape = this.shape.modifyCenter(pos);
		GraphicUtils.drawImage(g, actShape, ResourceManager.getInstance()
				.getImage(this.icon));
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
		GraphicUtils.drawScaled(g, actShape, new Vector(this.parent
				.getInterfaceable().getZoomX(), this.parent.getInterfaceable()
				.getZoomY()));
		GraphicUtils.drawString(g, pos, "" + this.name);
		g.setColor(c);
		// TODO: Beenden
	}

	@Override
	public Shape getPreferedSize() {
		return this.shape;
	}
}
