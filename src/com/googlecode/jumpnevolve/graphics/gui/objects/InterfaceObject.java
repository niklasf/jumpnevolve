/**
 *
 */
package com.googlecode.jumpnevolve.graphics.gui.objects;

import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.graphics.gui.Informable;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.InterfacePart;
import com.googlecode.jumpnevolve.graphics.gui.container.InterfaceContainer;
import com.googlecode.jumpnevolve.math.Rectangle;
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

	public final InterfaceFunction function;
	public InterfaceContainer parent;
	private int status;
	private boolean interfaceableAdded = false;

	protected ArrayList<Informable> toInform = new ArrayList<Informable>();

	private final int key;

	private static InterfaceObject LastSelected = null;

	/**
	 * @param function
	 *            Die Funktion dieses Objekts (ein Enum aus
	 *            {@link InterfaceFunctions})
	 */
	public InterfaceObject(InterfaceFunction function) {
		this(function, Input.KEY_ENTER);
	}

	public InterfaceObject(InterfaceFunction function, int key) {
		this.function = function;
		this.key = key;
	}

	public void setParentContainer(InterfaceContainer parent) {
		if (parent.contains(this)) {
			if (this.parent != null && this.parent.getInterfaceable() != null) {
				this.toInform.remove(this.parent.getInterfaceable());
			}
			this.parent = parent;
			if (this.parent.getInterfaceable() != null) {
				this.addInformable(this.parent.getInterfaceable());
				this.interfaceableAdded = true;
			}
		} else {
			Log.error("Parent enthält dieses Objekt nicht");
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
		if (this.getNeededSize()
				.modifyCenter(this.getCenterVector())
				.isPointInThis(new Vector(input.getMouseX(), input.getMouseY()))) {
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				onMouseDown(STATUS_PRESSED);
			} else if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				onMouseDown(STATUS_DOWN);
			} else {
				Object[] infos = this.toInform.toArray();
				for (Object informable : infos) {
					((Informable) informable).mouseOverAction(this);
				}
				this.status = STATUS_MOUSE_OVER;
			}
		} else {
			if (this.key != Input.KEY_ENTER && input.isKeyPressed(this.key)) {
				this.onMouseDown(STATUS_PRESSED);
			} else {
				this.status = STATUS_NOTHING;
			}
		}
		if (this.isSelected()) {
			Object[] infos = this.toInform.toArray();
			for (Object informable : infos) {
				((Informable) informable).objectIsSelected(this);
			}
		}
	}

	private void onMouseDown(int downOrPressed) {
		Object[] infos = this.toInform.toArray();
		this.status = downOrPressed;
		for (Object informable : infos) {
			((Informable) informable).mouseClickedAction(this);
		}
		LastSelected = this;
	}

	/**
	 * Fügt ein Informable hinzu, welches von nun an über Aktionen dieses
	 * Objekts informiert wird
	 * 
	 * @param object
	 *            Das Informable-Objekt
	 */
	public void addInformable(Informable object) {
		if (this.toInform.contains(object) == false && object != null) {
			this.toInform.add(object);
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

	public InterfaceFunction getFunction() {
		return this.function;
	}

	/**
	 * @return Die Position des Zentrum dieses Objekts auf der Oberfläche, ohne
	 *         die Translation durch die Kamera
	 */
	public Vector getCenterVector() {
		Rectangle size = this.getNeededSize();
		return this.parent.getPositionFor(this).add(
				new Vector(size.getDistanceToSide(Shape.LEFT), size
						.getDistanceToSide(Shape.UP)));
	}

	/**
	 * @param object
	 *            Das zu prüfende Objekt
	 * @return <code>true</code>, wenn als letztes auf dieses Objekt geklickt
	 *         wurde
	 */
	public boolean isSelected() {
		return this.equals(LastSelected);
	}
}
