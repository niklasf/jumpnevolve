package com.googlecode.jumpnevolve.graphics.gui;

import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 *         FIXME: Interface wird bei Zoom != 1 nicht richtig angezeigt
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
		// TODO Auto-generated constructor stub
	}

	public void setMainContainer(InterfaceContainer con) {
		this.remove(mainContainer);
		this.add(con, Vector.ZERO);
		this.mainContainer = con;
	}

	@Override
	public Shape getNeededSize() {
		return new Rectangle(Vector.ZERO, this.parentInterfaceable.getWidth(),
				this.parentInterfaceable.getHeight());
	}
}
