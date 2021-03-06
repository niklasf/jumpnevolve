/**
 *
 */
package com.googlecode.jumpnevolve.graphics.gui;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.gui.container.InterfaceContainer;
import com.googlecode.jumpnevolve.math.Rectangle;

/**
 * Interface wird von allem Klassen implementiert, die zum Interface gehören
 * 
 * @author Erik Wagner
 * 
 */
public interface InterfacePart extends Drawable, Pollable {

	/**
	 * @return Die Größe, die das Objekt belegen möchte, in Form eines Shapes,
	 *         dessen Position aber <b>keine</b> Bedeutung hat
	 */
	public Rectangle getNeededSize();

	/**
	 * Setzt das parent-Objekt dieses Objekts
	 * 
	 * @param parent
	 *            Das parent-Objekt
	 */
	public void setParentContainer(InterfaceContainer parent);
}
