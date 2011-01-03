/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class ButtonList extends InterfaceContainer implements Informable {

	private final int distanceBetweenButtons;
	private HashMap<Integer, Button> list = new HashMap<Integer, Button>();
	private HashMap<Button, Integer> invertList = new HashMap<Button, Integer>();
	private int next = 0;
	private int curPos = 0;
	private final int numberOfButtonsDisplayed;
	private Button back = new Button(this,
			InterfaceConstants.INTERFACE_BUTTONLIST_BACK,
			"interface-icons/back-arrow.png"), forth = new Button(this,
			InterfaceConstants.INTERFACE_BUTTONLIST_FORTH,
			"interface-icons/forth-arrow.png");
	private static final int BACK_POS = -2;
	private static final int FORTH_POS = -1;

	/**
	 * 
	 * @param parent
	 * @param numberOfButtonDisplayed
	 *            Anzahl der Buttons, die angezeigt werden sollen
	 */
	public ButtonList(InterfaceContainer parent, int numberOfButtonDisplayed,
			int distanceBetweenButtons) {
		super(parent);
		this.numberOfButtonsDisplayed = numberOfButtonDisplayed;
		this.distanceBetweenButtons = distanceBetweenButtons;
		this.list.put(BACK_POS, back);
		this.invertList.put(back, BACK_POS);
		this.list.put(FORTH_POS, forth);
		this.invertList.put(forth, FORTH_POS);
		back.addInformable(this);
		forth.addInformable(this);
	}

	public void addButton(Button object) {
		this.list.put(this.next, object);
		this.invertList.put(object, this.next);
		this.next = next + 1;
	}

	@Override
	public void poll(Input input, float secounds) {
		Object[] abbild = this.list.values().toArray();
		for (Object button : abbild) {
			((Button) button).poll(input, secounds);
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
		this.list.get(abbild[0]).draw(g);
		this.list.get(abbild[1]).draw(g);
	}

	/**
	 * Master
	 * 
	 * @param object
	 * @return
	 */
	public Vector getPositionFor(InterfacePart object) {
		if (object instanceof Button) {
			return getPositionForListPos(
					this.invertList.get(object) - this.curPos).add(
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
				(Button.BUTTON_DIMENSION + this.distanceBetweenButtons)
						* (listPos + 1), 0.0f);

	}

	@Override
	public void interfaceAction(int function, InterfaceObject object) {
		if (object.getStatus() == InterfaceObject.STATUS_PRESSED) {
			if (function == InterfaceConstants.INTERFACE_BUTTONLIST_FORTH) {
				this.moveForth();
			} else if (function == InterfaceConstants.INTERFACE_BUTTONLIST_BACK) {
				this.moveBack();
			}
		}
		System.out.println("Action");
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
}
