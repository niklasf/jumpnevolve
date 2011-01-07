/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Container für InterfaceObjects und andere InterfaceContainer
 * 
 * @author Erik Wagner
 * 
 */
public abstract class InterfaceContainer implements InterfacePart {

	protected HashMap<InterfacePart, Vector> objects = new HashMap<InterfacePart, Vector>();
	protected Interfaceable parentInterfaceable;
	protected InterfaceContainer parentContainer;

	public InterfaceContainer() {
	}

	public void setParentContainer(InterfaceContainer parent) {
		if (parent.contains(this)) {
			this.parentContainer = parent;
		} else {
			System.out.println("Parent enthält dieses Objekt nicht");
		}
	}

	public void setParentInterfaceable(Interfaceable parent) {
		this.parentInterfaceable = parent;
	}

	public boolean contains(InterfacePart object) {
		return this.objects.containsKey(object);
	}

	/**
	 * @return Das Interfaceable, dem dieser Container zugeordnet ist; {@code
	 *         null}, wenn es kein solches Interfaceable gibt, weil dieser
	 *         InterfaceContainer weder dirket einem Interfaceable zugeordnet
	 *         ist und auch keinem InterfaceContainer zugeordent ist, der einem
	 *         Interfaceable zugeordnet wurde
	 */
	public Interfaceable getInterfaceable() {
		if (this.parentInterfaceable == null) {
			if (this.parentContainer == null) {
				return null;
			} else {
				return this.parentContainer.getInterfaceable();
			}
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
		adding.setParentContainer(this);
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

	/**
	 * @param object
	 *            Das Objekt, nach dessen Platz gefragt wird
	 * @return Der Platz, der für das Objekt zur Verfügung steht
	 */
	public Rectangle getPlaceFor(InterfacePart object) {
		Vector pos = Vector.ZERO;
		if (this.parentContainer != null) {
			pos = this.parentContainer.getPositionFor(this);
		}
		return new Rectangle(Vector.ZERO, this.getInterfaceable().getWidth()
				- pos.x, this.getInterfaceable().getHeight() - pos.y);
	}
}
