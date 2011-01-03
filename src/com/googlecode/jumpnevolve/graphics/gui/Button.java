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
public class Button extends InterfaceObject {

	public static final float BUTTON_DIMENSION = 50.0f;

	private final String icon;

	/**
	 * Erzeigt einen neuen Button für das Interface
	 * 
	 * @param parent
	 *            Der InterfaceContainer, in dem dieser Button angezeigt werden
	 *            soll
	 * @param function
	 *            Die Funktion des Buttons (Eine der Konstanten aus
	 *            {@link InterfaceConstants});
	 * @param icon
	 *            Der Datei-Pfad des Icons, welches auf diesem Button
	 *            dargestellt werden soll
	 */
	public Button(InterfaceContainer parent, int function, String iconPath) {
		super(parent, new Rectangle(Vector.ZERO, BUTTON_DIMENSION,
				BUTTON_DIMENSION), function);
		this.icon = iconPath;
		System.out.println("Button erzeugt mit Funktion: " + function);
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
		// TODO: Beenden
	}
}
