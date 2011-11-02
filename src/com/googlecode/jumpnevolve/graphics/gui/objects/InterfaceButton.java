/**
 *
 */
package com.googlecode.jumpnevolve.graphics.gui.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * @author Erik Wagner
 * 
 */
public class InterfaceButton extends InterfaceObject {

	public static final float BUTTON_DIMENSION = Parameter.GUI_BUTTON_DIMENSION;

	private final String icon;
	private final Rectangle shape;

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
	public InterfaceButton(InterfaceFunction function, String iconPath) {
		super(function);
		this.icon = iconPath;
		this.shape = new Rectangle(Vector.ZERO, BUTTON_DIMENSION,
				BUTTON_DIMENSION);
	}

	@Override
	public void draw(Graphics g) {
		Vector pos = this.getCenterVector();
		Shape actShape = this.shape.modifyCenter(pos);
		GraphicUtils.drawImage(g, actShape, ResourceManager.getInstance()
				.getImage(this.icon));
		Color c = g.getColor();
		switch (this.getStatus()) {
		case STATUS_MOUSE_OVER:
			g.setColor(Color.yellow);
			GraphicUtils.drawString(g, pos, this.getFunction()
					.getFunctionName());
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
		GraphicUtils.drawScaled(g, actShape, this.parent.getInterfaceable()
				.getZoomX());
		g.setColor(c);
	}

	@Override
	public Rectangle getNeededSize() {
		return this.shape;
	}
}
