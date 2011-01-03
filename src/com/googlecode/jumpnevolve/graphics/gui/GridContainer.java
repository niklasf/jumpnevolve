/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class GridContainer extends InterfaceContainer {

	public static final int MODUS_DEFAULT = 0;
	public static final int MODUS_X_RIGHT = 1;
	public static final int MODUS_X_LEFT = 2;
	public static final int MODUS_Y_DOWN = 3;
	public static final int MODUS_Y_UP = 4;

	private final int modusX;
	private final int modusY;
	private final int rows;
	private final int cols;

	/**
	 * 
	 * @param parent
	 * @param rows
	 * @param cols
	 * @param modusX
	 * @param modusY
	 */
	public GridContainer(InterfaceContainer parent, int rows, int cols,
			int modusX, int modusY) {
		super(parent);
		this.rows = rows;
		this.cols = cols;
		this.modusX = modusX;
		this.modusY = modusY;
	}

	public GridContainer(InterfaceContainer parent, int rows, int cols) {
		this(parent, rows, cols, MODUS_DEFAULT, MODUS_DEFAULT);
	}

	/**
	 * Fügt dem GridManager ein InterfacePart in einer bestimmten Zelle hinzu
	 * 
	 * @param adding
	 *            Der hinzuzufügen Objekt
	 * @param row
	 *            Die Reihe der Zelle, die dieses Objekt als Grundlage nehmen
	 *            soll (beginnend mit 0)
	 * @param col
	 *            Die Spalte der Zelle, die dieses Objekt als Grundlage nehmen
	 *            soll (beginnend mit 0)
	 */
	public void add(InterfacePart adding, int row, int col) {
		super.add(adding, new Vector(col, row));
		/*
		 * int y = (int) ((this.getInterfaceable().getHeight() / (rows * 2))
		 * this.getYModifier() + this.getInterfaceable().getHeight() / rows *
		 * row); int x = (int) ((this.getInterfaceable().getWidth() / (cols *
		 * 2)) this.getXModifier() + this.getInterfaceable().getWidth() / cols *
		 * col); System.out.println("X: " + x + " Y: " + y + " H: " +
		 * this.getInterfaceable().getHeight() + " W: " +
		 * this.getInterfaceable().getWidth()); super.add(adding, new Vector(x,
		 * y));
		 */
	}

	@Override
	public void draw(Graphics g) {
		Object[] containerCopy = objects.keySet().toArray();
		for (Object interfaceContainer : containerCopy) {
			((InterfaceContainer) interfaceContainer).draw(g);
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		Object[] containerCopy = objects.keySet().toArray();
		for (Object interfaceContainer : containerCopy) {
			((InterfaceContainer) interfaceContainer).poll(input, secounds);
		}
	}

	@Override
	public Vector getPositionFor(InterfacePart object) {
		if (this.objects.containsKey(object)) {
			Vector cell = this.objects.get(object);
			int x = (int) ((this.getInterfaceable().getWidth() / (cols * 2))
					* this.getXModifier() + this.getInterfaceable().getWidth()
					/ cols * cell.x);
			int y = (int) ((this.getInterfaceable().getHeight() / (rows * 2))
					* this.getYModifier() + this.getInterfaceable().getHeight()
					/ rows * cell.y);
			return new Vector(x, y).add(this.parentContainer
					.getPositionFor(this));
		} else {
			return null;
		}
	}

	private float getXModifier() {
		switch (this.modusX) {
		case MODUS_X_LEFT:
			return 0.5f;
		case MODUS_X_RIGHT:
			return 1.5f;
		case MODUS_DEFAULT:
		default:
			return 1.0f;
		}
	}

	private float getYModifier() {
		switch (this.modusY) {
		case MODUS_Y_UP:
			return 0.5f;
		case MODUS_Y_DOWN:
			return 1.5f;
		case MODUS_DEFAULT:
		default:
			return 1.0f;
		}
	}
}
