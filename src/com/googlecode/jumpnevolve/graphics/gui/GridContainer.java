/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein GridContainer unterteilt die ihm zugeteilte Fläche in gleich große
 * Rechtecke. Objekte können in verschiedenen Modi angeordnet werden (
 * {@link #MODUS_DEFAULT}, {@link #MODUS_X_LEFT} oder {@link #MODUS_X_RIGHT},
 * {@link #MODUS_Y_DOWN} oder {@link #MODUS_Y_UP}).
 * 
 * @author Erik Wagner
 * 
 */
public class GridContainer extends InterfaceContainer {

	/**
	 * Die Objekte werden so platziert, dass die Mitte (in x-Richtung oder
	 * y-Richtung) der Mitte der entprechenden Zelle entspricht
	 */
	public static final int MODUS_DEFAULT = 0;

	/**
	 * Die Objekte werden so platziert, dass sie an der rechten Zellenseite
	 * anliegen
	 */
	public static final int MODUS_X_RIGHT = 1;

	/**
	 * Die Objekte werden so platziert, dass sie an der linken Zellenseite
	 * anliegen
	 */
	public static final int MODUS_X_LEFT = 2;

	/**
	 * Die Objekte werden so platziert, dass sie an der unteren Zellenseite
	 * anliegen
	 */
	public static final int MODUS_Y_DOWN = 3;

	/**
	 * Die Objekte werden so platziert, dass sie an der oberen Zellenseite
	 * anliegen
	 */
	public static final int MODUS_Y_UP = 4;

	private final int defaultModusX;
	private final int defaultModusY;
	private final int rows;
	private final int cols;
	private int[][] modiX;
	private int[][] modiY;

	/**
	 * Erzeugt einen neuen GridContainer, der seine Fläche in gleich große
	 * Zellen unterteilt, in denen die Objekte gemäß der Modi angeordnet werden
	 * 
	 * @param rows
	 *            Die Anzahl der Reihen Gitters
	 * @param cols
	 *            Die Anzahl der Spalten des Gittes
	 * @param modusX
	 *            Der Default-Modus für die X-Anordnung ({@link #MODUS_DEFAULT},
	 *            {@link #MODUS_X_LEFT} oder {@link #MODUS_X_RIGHT})
	 * @param modusY
	 *            Der Default-Modus für die Y-Anordnung ({@link #MODUS_DEFAULT},
	 *            {@link #MODUS_Y_DOWN} oder {@link #MODUS_Y_UP})
	 */
	public GridContainer(int rows, int cols, int modusX, int modusY) {
		super();
		this.rows = rows;
		this.cols = cols;
		this.defaultModusX = modusX;
		this.defaultModusY = modusY;
		this.modiX = new int[cols][rows];
		this.modiY = new int[cols][rows];
		for (int i = 0; i < this.modiX.length; i++) {
			for (int j = 0; j < this.modiX[i].length; j++) {
				this.modiX[i][j] = modusX;
			}
		}
		for (int i = 0; i < this.modiY.length; i++) {
			for (int j = 0; j < this.modiY[i].length; j++) {
				this.modiY[i][j] = modusY;
			}
		}
	}

	/**
	 * Erzeugt einen neuen GridContainer mit {@code modusX} =
	 * {@link #MODUS_DEFAULT} und {@code modusY} = {@link #MODUS_DEFAULT}
	 * 
	 * @see #GridContainer(int, int, int, int)
	 */
	public GridContainer(int rows, int cols) {
		this(rows, cols, MODUS_DEFAULT, MODUS_DEFAULT);
	}

	/**
	 * Fügt dem GridManager ein InterfacePart in einer bestimmten Zelle mit den
	 * Default-Modi hinzu
	 * 
	 * @see #add(InterfacePart, int, int, int, int)
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
		this.add(adding, row, col, this.defaultModusX, this.defaultModusY);
	}

	/**
	 * Fügt dem GridManager ein InterfacePart in einer bestimmten Zelle mit
	 * neuen Modi hinzu
	 * 
	 * @param adding
	 *            Der hinzuzufügen Objekt
	 * @param row
	 *            Die Reihe der Zelle, die dieses Objekt als Grundlage nehmen
	 *            soll (beginnend mit 0)
	 * @param col
	 *            Die Spalte der Zelle, die dieses Objekt als Grundlage nehmen
	 *            soll (beginnend mit 0)
	 * @param modusX
	 *            Der Modus in x-Richtung für die Zelle, in die das Objekt
	 *            geaddet wird
	 * @param modusY
	 *            Der Modus in y-Richtung für die Zelle, in die das Objekt
	 *            geaddet wird
	 */
	public void add(InterfacePart adding, int row, int col, int modusX,
			int modusY) {
		this.modiX[col][row] = modusX;
		this.modiY[col][row] = modusY;
		super.add(adding, new Vector(col, row));
	}

	@Override
	public void draw(Graphics g) {
		Object[] containerCopy = objects.keySet().toArray();
		for (Object interfacePart : containerCopy) {
			((InterfacePart) interfacePart).draw(g);
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		Object[] containerCopy = objects.keySet().toArray();
		for (Object interfacePart : containerCopy) {
			((InterfacePart) interfacePart).poll(input, secounds);
		}
	}

	@Override
	public Vector getPositionFor(InterfacePart object) {
		Rectangle place = this.parentContainer.getPlaceFor(this);
		if (this.objects.containsKey(object)) {
			Vector cell = this.objects.get(object);
			Shape shape = object.getPreferedSize();
			int x = (int) (this.getXPosInCell((int) cell.x, (int) cell.y,
					place.width / cols, shape.getXRange()) + place.width / cols
					* cell.x);
			int y = (int) (this.getYPosInCell((int) cell.x, (int) cell.y,
					place.height / rows, shape.getYRange()) + place.height
					/ rows * cell.y);
			return new Vector(x, y).add(this.parentContainer
					.getPositionFor(this));
		} else {
			return null;
		}
	}

	/**
	 * Liefert die x-Koordinate für das Objekt in der Zelle gemäß dem
	 * eingestellen x-Modus
	 */
	private float getXPosInCell(int col, int row, float cellWidth,
			float objectWidth) {
		switch (this.modiX[col][row]) {
		case MODUS_X_LEFT:
			return 0;
		case MODUS_X_RIGHT:
			return cellWidth - objectWidth;
		case MODUS_DEFAULT:
		default:
			return cellWidth / 2 - objectWidth / 2;
		}
	}

	/**
	 * Liefert die y-Koordinate für das Objekt in der Zelle gemäß dem
	 * eingestellen y-Modus
	 */
	private float getYPosInCell(int col, int row, float cellHeight,
			float objectHeight) {
		switch (this.modiY[col][row]) {
		case MODUS_Y_UP:
			return 0;
		case MODUS_Y_DOWN:
			return cellHeight - objectHeight;
		case MODUS_DEFAULT:
		default:
			return cellHeight / 2 - objectHeight / 2;
		}
	}

	@Override
	public Shape getPreferedSize() {
		return this.parentContainer.getPlaceFor(this);
	}

	@Override
	public Rectangle getPlaceFor(InterfacePart object) {
		// Die Größe einer Zelle
		Rectangle place = this.parentContainer.getPlaceFor(this);
		return new Rectangle(Vector.ZERO, place.width / cols, place.height
				/ rows);
	}
}
