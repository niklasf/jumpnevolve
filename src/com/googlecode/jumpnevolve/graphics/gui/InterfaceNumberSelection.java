package com.googlecode.jumpnevolve.graphics.gui;

/**
 * @author e.wagner
 * 
 *         TODO: Maximum und Minimum für curNumber sollten eingefügt werden
 */
public class InterfaceNumberSelection extends GridContainer implements
		Informable {

	private InterfaceLabel label = new InterfaceLabel("0", 12);
	private int curNumber = 0;

	public InterfaceNumberSelection() {
		super(1, 3);
		InterfaceButton a = new InterfaceButton(
				InterfaceFunctions.INTERFACE_NUMBER_SELECTION_BACK,
				"interface-icons/back-arrow.png");
		InterfaceButton b = new InterfaceButton(
				InterfaceFunctions.INTERFACE_NUMBER_SELECTION_FORTH,
				"interface-icons/forth-arrow.png");
		a.addInformable(this);
		b.addInformable(this);
		this.add(a, 0, 0, MODUS_X_LEFT, MODUS_DEFAULT);
		this.add(b, 0, 2, MODUS_X_RIGHT, MODUS_DEFAULT);
		this.add(this.label, 0, 1);
	}

	private void moveBack() {
		this.curNumber--;
		this.updateLabel();
	}

	private void moveForth() {
		this.curNumber++;
		this.updateLabel();
	}

	private void updateLabel() {
		this.label.setText("" + this.curNumber);
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
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		// Nichts tun
	}
}