/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Container für InterfaceObjects und andere InterfaceContainer
 * 
 * @author Erik Wagner
 * 
 */
public abstract class InterfaceContainer implements InterfacePart {

	protected HashMap<InterfacePart, Vector> objects = new HashMap<InterfacePart, Vector>();
	protected final Interfaceable parentInterfaceable;
	protected final InterfaceContainer parentContainer;

	/**
	 * 
	 */
	public InterfaceContainer(Interfaceable parent) {
		this.parentInterfaceable = parent;
		this.parentContainer = null;
	}

	public InterfaceContainer(InterfaceContainer parent) {
		this.parentContainer = parent;
		this.parentInterfaceable = null;
	}

	public Interfaceable getInterfaceable() {
		if (this.parentInterfaceable == null) {
			return this.parentContainer.getInterfaceable();
		} else {
			return this.parentInterfaceable;
		}
	}

	/**
	 * Fügt ein InterfacePart diesem Container hinzu
	 * 
	 * @param adding
	 *            Das hinzuzufügende Objekt
	 * @param relativePositionOnScreen
	 *            Die linke, obere Ecke des Objekts auf der Ausgabefläche
	 */
	protected void add(InterfacePart adding, Vector relativePositionOnScreen) {
		this.objects.put(adding, relativePositionOnScreen);
	}

	protected void remove(InterfacePart removing) {
		this.objects.remove(removing);
	}

	@Override
	public void draw(Graphics g) {
		Object[] objectsCopy = objects.keySet().toArray();
		for (Object object : objectsCopy) {
			((InterfacePart) object).draw(g);
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		Object[] objectsCopy = objects.keySet().toArray();
		for (Object object : objectsCopy) {
			((InterfacePart) object).poll(input, secounds);
		}
	}

	/**
	 * @param object
	 *            Das Objekt, dessen Position angefragt wird
	 * @return Position der oberen, linken Ecke des Objekts; {@code null}, wenn
	 *         das Objekt nicht in diesem Container enthalten ist
	 */
	public Vector getPositionFor(InterfacePart object) {
		if (this.parentInterfaceable == null) {
			if (this.objects.containsKey(object)) {
				return this.objects.get(object).add(
						this.parentContainer.getPositionFor(this));
			} else {
				return null;
			}
		} else {
			if (this.objects.containsKey(object)) {
				return this.objects.get(object);
			} else {
				return null;
			}
		}
	}
}
