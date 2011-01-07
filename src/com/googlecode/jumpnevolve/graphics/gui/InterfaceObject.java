/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine Superklasse für Objekte im Interface. Gibt die Ereignisse an sein
 * "Parent" weiter und außerdem an {@link Informable}, wenn diese mit
 * {@link addInformable} hinzugefügt wurden
 * 
 * @author Erik Wagner
 * 
 */
public abstract class InterfaceObject implements InterfacePart {

	/**
	 * Die Maus befindet sich nicht über dem Objekt
	 */
	public static final int STATUS_NOTHING = 0;
	/**
	 * Es wurde gerade mit der linken Maustaste auf das Objekt geklickt
	 */
	public static final int STATUS_PRESSED = 1;
	/**
	 * Die Maus befindet sich über dem Objekt, ohne dass die linke Maustaste
	 * gedrückt wurde
	 */
	public static final int STATUS_MOUSE_OVER = 2;
	/**
	 * Die Maus befindet sich über dem Objekt, die linke Maustaste ist gedrückt
	 */
	public static final int STATUS_DOWN = 3;

	public final int function;
	public InterfaceContainer parent;
	private int status;
	private boolean wasClicked = false, interfaceableAdded = false;

	private ArrayList<Informable> informed = new ArrayList<Informable>();

	/**
	 * @param parent
	 *            Das Interfaceable, das dieses Objekt beinhaltet
	 * @param shape
	 *            Das Shape, welches dieses Objekt in seiner Form beschreibt
	 * @param function
	 *            Die Funktion dieses Objekts (eine Konstante aus
	 *            {@link InterfaceConstants})
	 */
	public InterfaceObject(int function) {
		this.function = function;
	}

	public void setParentContainer(InterfaceContainer parent) {
		if (parent.contains(this)) {
			if (this.parent != null && this.parent.getInterfaceable() != null) {
				this.informed.remove(this.parent.getInterfaceable());
			}
			this.parent = parent;
			if (this.parent.getInterfaceable() != null) {
				this.addInformable(this.parent.getInterfaceable());
				this.interfaceableAdded = true;
			}
		} else {
			System.out.println("Parent enthält dieses Objekt nicht");
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		if (this.interfaceableAdded == false) {
			if (this.parent.getInterfaceable() != null) {
				this.addInformable(this.parent.getInterfaceable());
				this.interfaceableAdded = true;
			}
		}
		if (this
				.getPreferedSize()
				.modifyCenter(this.getCenterVector())
				.isPointInThis(new Vector(input.getMouseX(), input.getMouseY()))) {
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				Object[] infos = this.informed.toArray();
				for (Object informable : infos) {
					((Informable) informable).mouseClickedAction(this);
				}
				if (wasClicked) {
					this.status = STATUS_DOWN;
				} else {
					this.status = STATUS_PRESSED;
				}
				this.wasClicked = true;
			} else {
				Object[] infos = this.informed.toArray();
				for (Object informable : infos) {
					((Informable) informable).mouseOverAction(this);
				}
				this.status = STATUS_MOUSE_OVER;
				this.wasClicked = false;
			}
		} else {
			this.status = STATUS_NOTHING;
			this.wasClicked = false;
		}
	}

	/**
	 * Fügt ein Informable hinzu, welches von nun an über Aktionen dieses
	 * Objekts informiert wird
	 * 
	 * @param object
	 *            Das Informable-Objekt
	 */
	public void addInformable(Informable object) {
		if (this.informed.contains(object) == false && object != null) {
			this.informed.add(object);
		}
	}

	/**
	 * @return Der Status des Objekts, eine der Konstanten aus
	 *         {@link InterfaceObject}: {@link STATUS_NOTHING},
	 *         {@link STATUS_PRESSED}, {@link STATUS_MOUSE_OVER},
	 *         {@link STATUS_DOWN}
	 */
	public int getStatus() {
		return this.status;
	}

	public int getFunction() {
		return this.function;
	}

	/**
	 * @return Die Position des Zentrum dieses Objekts auf der Oberfläche, ohne
	 *         die Translation durch die Kamera
	 */
	public Vector getCenterVector() {
		return this.parent.getPositionFor(this).add(
				new Vector(this.getPreferedSize()
						.getDistanceToSide(Shape.LEFT), this.getPreferedSize()
						.getDistanceToSide(Shape.UP)));
	}
}
