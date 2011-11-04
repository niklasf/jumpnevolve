package com.googlecode.jumpnevolve.graphics.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.gui.container.InterfaceContainer;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Die Hauptklasse für das User-Interface.
 * <p>
 * Es wird ein MainContainer gesetzt, in diesen können andere
 * {@link InterfacePart} eingefügt werden.
 * <p>
 * poll() und draw() werden für das MainGUI aufgerufen und an die anderen
 * Objekte weitergegeben.
 * <p>
 * draw(): es wird unabhängig von vorherigen Translationen/Sklierungen etc.
 * gearbeitet, sodass das Interface immer gleich aussieht.
 * 
 * @author Erik Wagner
 * 
 */
public class MainGUI extends InterfaceContainer {

	private InterfaceContainer mainContainer;

	/**
	 * @param parent
	 */
	public MainGUI(Interfaceable parent) {
		super();
		this.setParentInterfaceable(parent);
	}

	public void setMainContainer(InterfaceContainer con) {
		this.remove(mainContainer);
		this.add(con, Vector.ZERO);
		this.mainContainer = con;
	}

	public InterfaceContainer getMainContainer() {
		return this.mainContainer;
	}

	@Override
	public Rectangle getWantedSize() {
		return new Rectangle(Vector.ZERO, this.parentInterfaceable.getWidth(),
				this.parentInterfaceable.getHeight());
	}

	@Override
	public void draw(Graphics g) {
		g.pushTransform();
		g.resetTransform();
		super.draw(g);
		g.popTransform();
	}
}
