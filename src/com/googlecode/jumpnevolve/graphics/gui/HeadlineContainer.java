package com.googlecode.jumpnevolve.graphics.gui;

import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein InterfaceContainer, der die ihm zur Verfügung stehende Fläche in eine
 * Kopfzeile und einen darunterliegenden Hauptteil aufteilt
 * 
 * @author Erik Wagner
 * 
 */
public class HeadlineContainer extends InterfaceContainer {

	private InterfaceContainer headlineCon, mainCon;

	/**
	 * Erzeugt einen neuen HeadlineContainer
	 * 
	 * @param headlineCon
	 *            Der InterfaceContainer, der die Kopfzeilen-Fläche einnimmt
	 * @param mainCon
	 *            Der InterfaceContainer, der die Hauptteil-Fläche belegt
	 */
	public HeadlineContainer(InterfaceContainer headlineCon,
			InterfaceContainer mainCon) {
		this.setHeadlineContainer(headlineCon);
		this.setMainContainer(mainCon);
	}

	public void setHeadlineContainer(InterfaceContainer con) {
		this.remove(this.headlineCon);
		this.headlineCon = con;
		this.add(this.headlineCon, Vector.ZERO);
	}

	public void setMainContainer(InterfaceContainer con) {
		this.remove(this.mainCon);
		this.mainCon = con;
		this.add(this.mainCon, new Vector(0, this.headlineCon.getNeededSize()
				.getYRange()));
	}

	public InterfaceContainer getMainContainer() {
		return this.mainCon;
	}

	public InterfaceContainer getHeadlineContainer() {
		return this.headlineCon;
	}

	@Override
	public Shape getNeededSize() {
		Shape head = this.headlineCon.getNeededSize(), main = this.mainCon
				.getNeededSize();
		return new Rectangle(Vector.ZERO, Math.max(head.getXRange(),
				main.getXRange()), head.getYRange() + main.getYRange());
	}

	public Rectangle getPlaceFor(InterfacePart object) {
		if (object == this.headlineCon) {
			return super.getPlaceFor(object);
		} else if (object == this.mainCon) {
			Rectangle place = super.getPlaceFor(object);
			return new Rectangle(Vector.ZERO, place.width, place.height
					- this.headlineCon.getNeededSize().getYRange());
		} else {
			return super.getPlaceFor(object);
		}
	}
}
