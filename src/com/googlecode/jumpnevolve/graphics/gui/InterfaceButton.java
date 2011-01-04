/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

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

	/**
	 * Erzeigt einen neuen Button für das Interface
	 * 
	 * @param function
	 *            Die Funktion des Buttons (Eine der Konstanten aus
	 *            {@link InterfaceConstants});
	 * @param icon
	 *            Der Datei-Pfad des Icons, welches auf diesem Button
	 *            dargestellt werden soll
	 */
	public InterfaceButton(int function, String iconPath) {
		super(new Rectangle(Vector.ZERO, BUTTON_DIMENSION, BUTTON_DIMENSION),
				function);
		this.icon = iconPath;
		System.out.println("Button erzeugt mit Funktion: " + function);
	}

	public InterfaceButton(int function, String iconPath, char name) {
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
		GraphicUtils.drawScaled(g, actShape, new Vector(this.parent
				.getInterfaceable().getZoomX(), this.parent.getInterfaceable()
				.getZoomY()));
		GraphicUtils.string(g, pos, "" + this.name);
		// TODO: Beenden
	}

	@Override
	public Shape getPrefferedSize() {
		return new Rectangle(Vector.ZERO, BUTTON_DIMENSION, BUTTON_DIMENSION);
	}
}
