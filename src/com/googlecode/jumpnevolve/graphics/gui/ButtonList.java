/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class ButtonList extends InterfaceContainer implements Informable {

	private final int distanceBetweenButtons;
	private HashMap<Integer, InterfaceButton> list = new HashMap<Integer, InterfaceButton>();
	private HashMap<InterfaceButton, Integer> invertList = new HashMap<InterfaceButton, Integer>();
	private int next = 0;
	private int curPos = 0;
	private final int numberOfButtonsDisplayed;
	private static final int BACK_POS = -2;
	private static final int FORTH_POS = -1;

	/**
	 * 
	 * @param parent
	 * @param numberOfButtonDisplayed
	 *            Anzahl der Buttons, die angezeigt werden sollen
	 */
	public ButtonList(int numberOfButtonDisplayed, int distanceBetweenButtons) {
		super();
		this.numberOfButtonsDisplayed = numberOfButtonDisplayed;
		this.distanceBetweenButtons = distanceBetweenButtons;
		InterfaceButton back = new InterfaceButton(
				InterfaceFunctions.INTERFACE_BUTTONLIST_BACK,
				"interface-icons/back-arrow.png"), forth = new InterfaceButton(
				InterfaceFunctions.INTERFACE_BUTTONLIST_FORTH,
				"interface-icons/forth-arrow.png");
		this.list.put(BACK_POS, back);
		this.invertList.put(back, BACK_POS);
		this.add(back, Vector.ZERO);
		this.list.put(FORTH_POS, forth);
		this.invertList.put(forth, FORTH_POS);
		this.add(forth, Vector.ZERO);
		back.addInformable(this);
		forth.addInformable(this);
	}

	public void addButton(InterfaceButton object) {
		this.list.put(this.next, object);
		this.invertList.put(object, this.next);
		this.next = next + 1;
		this.add(object, Vector.ZERO);
	}

	@Override
	public void poll(Input input, float secounds) {
		Object[] abbild = this.list.values().toArray();
		for (Object button : abbild) {
			((InterfaceButton) button).poll(input, secounds);
		}
	}

	@Override
	public void draw(Graphics g) {
		Object[] abbild = this.list.keySet().toArray();
		java.util.Arrays.sort(abbild);
		for (int i = this.curPos + 2; i < this.curPos
				+ numberOfButtonsDisplayed + 2
				&& i < abbild.length; i++) {
			this.list.get(abbild[i]).draw(g);
		}
		if (this.isBackMoveable()) {
			this.list.get(abbild[0]).draw(g);
		}
		if (this.isForthMoveable()) {
			this.list.get(abbild[1]).draw(g);
		}
	}

	public Vector getPositionFor(InterfacePart object) {
		if (object instanceof InterfaceButton) {
			int listPos = this.invertList.get(object);
			if (listPos != BACK_POS && listPos != FORTH_POS) {
				if (listPos < this.curPos) {
					return new Vector(-InterfaceButton.BUTTON_DIMENSION * 10, 0);
				}
				if (listPos >= this.curPos + this.numberOfButtonsDisplayed) {
					return new Vector(-InterfaceButton.BUTTON_DIMENSION * 10, 0);
				}
			}
			listPos = listPos - this.curPos;
			return getPositionForListPos(listPos).add(
					this.parentContainer.getPositionFor(this));
		} else {
			return super.getPositionFor(object);
		}
	}

	private Vector getPositionForListPos(int listPos) {
		if (listPos + this.curPos == BACK_POS) {
			listPos = -1;
		} else if (listPos + this.curPos == FORTH_POS) {
			listPos = this.numberOfButtonsDisplayed;
		}
		return new Vector(
				(InterfaceButton.BUTTON_DIMENSION + this.distanceBetweenButtons)
						* (listPos + 1), 0.0f);

	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		if (object.getStatus() == InterfaceObject.STATUS_PRESSED) {
			if (object.getFunction() == InterfaceFunctions.INTERFACE_BUTTONLIST_FORTH) {
				this.moveForth();
			} else if (object.getFunction() == InterfaceFunctions.INTERFACE_BUTTONLIST_BACK) {
				this.moveBack();
			}
		}
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		// Nichts tun
	}

	private void moveBack() {
		if (this.curPos > 0) {
			this.curPos--;
		}
	}

	private void moveForth() {
		if (this.curPos < this.list.keySet().toArray().length - 2
				- numberOfButtonsDisplayed) {
			this.curPos++;
		}
	}

	private boolean isForthMoveable() {
		return this.list.keySet().toArray().length - 2 > numberOfButtonsDisplayed
				+ curPos;
	}

	private boolean isBackMoveable() {
		return this.curPos > 0;
	}

	@Override
	public Shape getPreferedSize() {
		int z = 1;
		if (this.isForthMoveable()) {
			z++;
		}
		if (this.isBackMoveable()) {
			z++;
		}
		float width = (InterfaceButton.BUTTON_DIMENSION + this.distanceBetweenButtons)
				* (this.numberOfButtonsDisplayed + z)
				- this.distanceBetweenButtons;
		return new Rectangle(Vector.ZERO, width,
				InterfaceButton.BUTTON_DIMENSION);
	}
}
