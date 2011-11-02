package com.googlecode.jumpnevolve.graphics.gui.objects;

import com.googlecode.jumpnevolve.graphics.gui.Contentable;
import com.googlecode.jumpnevolve.graphics.gui.Informable;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.container.GridContainer;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * Eine Zahlenauswahl für Dialoge u.Ä.
 * 
 * @author e.wagner
 */
public class InterfaceNumberSelection extends GridContainer implements
		Informable, Contentable {

	private static final int DEFAULT_STEP = Parameter.GUI_NUMBERSELECTION_DEFAULTSTEP;

	private InterfaceTextField textField = new InterfaceTextField(
			InterfaceFunctions.INTERFACE_TEXTFIELD);
	private int curNumber;
	private final int min, max, step;

	public InterfaceNumberSelection(int min, int max, int start, int step) {
		super(1, 3);
		this.min = min;
		this.max = max;
		if (step > 0) { // Keine negativen Steps und nicht 0
			this.step = step;
		} else {
			this.step = DEFAULT_STEP;
		}
		this.curNumber = start;
		InterfaceButton a = new InterfaceButton(
				InterfaceFunctions.INTERFACE_NUMBER_SELECTION_BACK,
				"interface-icons/back-arrow.png");
		InterfaceButton b = new InterfaceButton(
				InterfaceFunctions.INTERFACE_NUMBER_SELECTION_FORTH,
				"interface-icons/forth-arrow.png");
		a.addInformable(this);
		b.addInformable(this);
		this.textField.addInformable(this);
		this.add(a, 0, 0, MODUS_X_LEFT, MODUS_DEFAULT);
		this.add(b, 0, 2, MODUS_X_RIGHT, MODUS_DEFAULT);
		this.add(this.textField, 0, 1);
		this.updateTextField();
	}

	public InterfaceNumberSelection(int min, int max) {
		this(min, max, min, DEFAULT_STEP);
	}

	private void moveBack() {
		if (this.curNumber > this.min) {
			this.curNumber = this.curNumber - this.step;
			this.updateTextField();
		}
	}

	private void moveForth() {
		if (this.curNumber < this.max) {
			this.curNumber = this.curNumber + this.step;
			this.updateTextField();
		}
	}

	private void updateTextField() {
		this.textField.setContent("" + this.curNumber);
	}

	private void transmitTextFieldContent(InterfaceObject object) {
		String content = this.textField.getContent();
		int number = 0;
		if (content.length() > 0) {
			try {
				number = Integer.parseInt(content);
			} catch (NumberFormatException e) {
				// Nichts tun
			}
		}
		if (number >= this.min && number <= this.max) {
			this.curNumber = number;
		}
		this.updateTextField();
	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		if (object.getStatus() == InterfaceObject.STATUS_PRESSED) {
			if (object.getFunction() == InterfaceFunctions.INTERFACE_NUMBER_SELECTION_FORTH) {
				this.moveForth();
			} else if (object.getFunction() == InterfaceFunctions.INTERFACE_NUMBER_SELECTION_BACK) {
				this.moveBack();
			}
		}
		if (object.getFunction() == InterfaceFunctions.INTERFACE_TEXTFIELD) {
			this.transmitTextFieldContent(object);
		}
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		if (object.getFunction() == InterfaceFunctions.INTERFACE_TEXTFIELD) {
			this.transmitTextFieldContent(object);
		}
	}

	@Override
	public void objectIsSelected(InterfaceObject object) {
		if (object.getFunction() == InterfaceFunctions.INTERFACE_TEXTFIELD) {
			this.transmitTextFieldContent(object);
		}
	}

	@Override
	public Rectangle getNeededSize() {
		return new Rectangle(Vector.ZERO, InterfaceButton.BUTTON_DIMENSION * 2
				+ this.textField.getNeededSize().getXRange(),
				InterfaceButton.BUTTON_DIMENSION + 10);
	}

	@Override
	public String getContent() {
		return "" + curNumber;
	}

	@Override
	public void setContent(String newContent) {
		this.curNumber = (int) (Float.parseFloat(newContent.trim()));
		this.updateTextField();
	}
}
