/**
 *
 */
package com.googlecode.jumpnevolve.graphics.gui;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Container für InterfaceObjects und andere InterfaceContainer
 * 
 * @author Erik Wagner
 * 
 */
public abstract class InterfaceContainer implements InterfacePart {

	private static final int MAXIME_NOTHING = 0;
	private static final int MAXIME_X = 1;
	private static final int MAXIME_Y = 2;
	private static final int MAXIME_BOTH = 3;

	protected HashMap<InterfacePart, Vector> objects = new HashMap<InterfacePart, Vector>();
	protected Interfaceable parentInterfaceable;
	protected InterfaceContainer parentContainer;
	private boolean backgroundState = false;
	private Color backgroundColor = Color.black;
	protected float sizeX = Float.NaN, sizeY = Float.NaN;

	private int toMaximize = MAXIME_NOTHING;

	public abstract Rectangle getWantedSize();

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

	/**
	 * Setzt die Größe dieses Containers auf einen festen Wert
	 * 
	 * @param size
	 *            Die Größe des Containers
	 */
	public void setSize(Rectangle size) {
		this.sizeX = size.width;
		this.sizeY = size.height;
	}

	/**
	 * Maximiert die Größe des Containers anhand der ihm zugewiesenen Position
	 * und der Größe des Interfaceables
	 * 
	 * Es wird das Rechteck zwischen Position und unterer rechter Ecke des
	 * Interfaceables als Size gesetzt
	 */
	public void maximizeSize() {
		this.toMaximize = MAXIME_BOTH;
		Vector pos = Vector.ZERO;
		Interfaceable inter = this.getInterfaceable();
		if (inter != null) {
			if (this.parentContainer != null) {
				pos = this.parentContainer.getPositionFor(this);
			}
			this.sizeX = inter.getWidth() - pos.x;
			this.sizeY = inter.getHeight() - pos.y;
			this.toMaximize = MAXIME_NOTHING;
		}
	}

	/**
	 * Maximiert die Größe des Containers auf der X-Achse anhand der ihm
	 * zugewiesenen Position und der Größe des Interfaceables
	 * 
	 * Es wird die maximal verfügbare Breite gewählt und die Höhe über
	 * getWantedSize() ermittelt
	 */
	public void maximizeXRange() {
		this.toMaximize = MAXIME_X;
		Vector pos = Vector.ZERO;
		Interfaceable inter = this.getInterfaceable();
		if (inter != null) {
			if (this.parentContainer != null) {
				pos = this.parentContainer.getPositionFor(this);
			}
			this.sizeX = inter.getWidth() - pos.x + 1;
			this.sizeY = Float.NaN;
			this.toMaximize = MAXIME_NOTHING;
		}
	}

	/**
	 * Maximiert die Größe des Containers auf der Y-Achse anhand der ihm
	 * zugewiesenen Position und der Größe des Interfaceables
	 * 
	 * Es wird die maximal verfügbare Höhe gewählt und die Breite über
	 * getWantedSize() ermittelt
	 */
	public void maximizeYRange() {
		this.toMaximize = MAXIME_Y;
		Vector pos = Vector.ZERO;
		Interfaceable inter = this.getInterfaceable();
		if (inter != null) {
			if (this.parentContainer != null) {
				pos = this.parentContainer.getPositionFor(this);
			}
			this.sizeX = Float.NaN;
			this.sizeY = inter.getHeight() - pos.y + 1;
			this.toMaximize = MAXIME_NOTHING;
		}
	}

	public void enableBackground() {
		this.setBackgroundState(true);
	}

	public void disableBackground() {
		this.setBackgroundState(false);
	}

	public void setBackgroundState(boolean state) {
		this.backgroundState = state;
	}

	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}

	public boolean contains(InterfacePart object) {
		return this.objects.containsKey(object);
	}

	/**
	 * @return Das Interfaceable, dem dieser Container zugeordnet ist;
	 *         {@code null}, wenn es kein solches Interfaceable gibt, weil
	 *         dieser InterfaceContainer weder dirket einem Interfaceable
	 *         zugeordnet ist und auch keinem InterfaceContainer zugeordent ist,
	 *         der einem Interfaceable zugeordnet wurde
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

	private void checkMaximizing() {
		switch (this.toMaximize) {
		case MAXIME_BOTH:
			this.maximizeSize();
			break;
		case MAXIME_X:
			this.maximizeXRange();
			break;
		case MAXIME_Y:
			this.maximizeYRange();
			break;
		default:
			break;
		}
	}

	private void drawBackground(Graphics g) {
		if (this.parentContainer != null) {
			Shape shape = this.getNeededSize();
			shape = shape.modifyCenter(this.parentContainer
					.getTransformedPositionFor(this).add(
							new Vector(shape.getXRange(), shape.getYRange())
									.div(2.0f)));
			GraphicUtils.fill(g, shape, this.backgroundColor);
		}
	}

	@Override
	public void draw(Graphics g) {
		// Container auf ausstehende Maximierungen überprüfen
		this.checkMaximizing();

		// Hintergrund einfärben, wenn gewünscht
		if (this.backgroundState) {
			this.drawBackground(g);

		}

		// Im Container enthaltene Objekte zeichnen
		Object[] objectsCopy = objects.keySet().toArray();
		for (Object object : objectsCopy) {
			((InterfacePart) object).draw(g);
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		// Im Container enthaltene Objekte pollen
		Object[] containerCopy = objects.keySet().toArray();
		for (Object interfacePart : containerCopy) {
			((InterfacePart) interfacePart).poll(input, secounds);
		}
	}

	@Override
	public Rectangle getNeededSize() {
		float width = this.sizeX, height = this.sizeY;
		// Doppelte Abfrage ob eine Variable gleich NaN ist, ist schneller als
		// in jedem Fall getWantedSize() aufzurufen
		if (Float.isNaN(width) || Float.isNaN(height)) {
			Rectangle wanted = this.getWantedSize();
			if (Float.isNaN(width)) {
				width = wanted.width;
			}
			if (Float.isNaN(height)) {
				height = wanted.height;
			}
		}
		return new Rectangle(Vector.ZERO, width, height);
	}

	/**
	 * @param object
	 *            Das Objekt, dessen Position angefragt wird
	 * @return Position der oberen, linken Ecke des Objekts auf der Oberfläche
	 *         (ohne Translation durch die Kamera); {@code null}, wenn das
	 *         Objekt nicht in diesem Container enthalten ist
	 */
	public Vector getPositionFor(InterfacePart object) {
		if (this.objects.containsKey(object)) {
			if (this.parentInterfaceable == null
					&& this.parentContainer != null) {
				return this.objects.get(object).add(
						this.parentContainer.getPositionFor(this));
			} else {
				return this.objects.get(object);
			}
		} else {
			return null;
		}
	}

	/**
	 * @param object
	 *            Das Objekt, dessen Position angefragt wird
	 * @return Position der oberen, linken Ecke des Objekts auf der
	 *         Zeichenfläche (mit Translation durch die Kamera); {@code null},
	 *         wenn das Objekt nicht in diesem Container enthalten ist
	 */
	public Vector getTransformedPositionFor(InterfacePart object) {
		Vector pos = this.getPositionFor(object);
		if (pos != null) {
			return pos.add(this
					.getInterfaceable()
					.getCamera()
					.getPosition()
					.sub(new Vector(this.getInterfaceable().getWidth() / 2,
							this.getInterfaceable().getHeight() / 2)));
		} else {
			return null;
		}
	}
}
