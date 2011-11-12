/**
 *
 */
package com.googlecode.jumpnevolve.graphics.gui.container;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.gui.Informable;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.InterfacePart;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceTextButton;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class TextButtonList extends InterfaceContainer implements Informable {

	private final int distanceBetweenButtons;
	private HashMap<Integer, InterfaceTextButton> list = new HashMap<Integer, InterfaceTextButton>();
	private HashMap<InterfaceTextButton, Integer> invertList = new HashMap<InterfaceTextButton, Integer>();
	private int next = 0;
	private int curPos = 0;
	private final int numberOfButtonsDisplayed;
	private float maxWidth = 1;

	private static final int DOWN_POS = -2;
	private static final int UP_POS = -1;

	/**
	 *
	 */
	public TextButtonList(int numberOfButtonsDisplayed,
			int distanceBetweenButtons) {
		this.numberOfButtonsDisplayed = numberOfButtonsDisplayed;
		this.distanceBetweenButtons = distanceBetweenButtons;
		InterfaceTextButton up = new InterfaceTextButton(
				InterfaceFunctions.INTERFACE_TEXTBUTTONLIST_UP, "Nach oben"), down = new InterfaceTextButton(
				InterfaceFunctions.INTERFACE_TEXTBUTTONLIST_DOWN, "Nach unten");
		this.list.put(UP_POS, up);
		this.invertList.put(up, UP_POS);
		this.add(up, Vector.ZERO);
		this.list.put(DOWN_POS, down);
		this.invertList.put(down, DOWN_POS);
		this.add(down, Vector.ZERO);
		up.addInformable(this);
		down.addInformable(this);
		// TODO: Up- und Down-Button sollten für maxWidth betrachtet werden,
		// dies kann jedoch nicht hier geschehen, da sonst deren Größe aufgrund
		// der fehlenden Font noch nicht initialisiert ist
	}

	public void addTextButton(InterfaceTextButton button) {
		this.list.put(this.next, button);
		this.invertList.put(button, this.next);
		this.next = next + 1;
		this.add(button, Vector.ZERO);
	}

	public void poll(Input input, float secounds) {
		Object[] abbild = this.list.values().toArray();
		for (Object button : abbild) {
			((InterfaceTextButton) button).poll(input, secounds);
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
		if (this.isDownMoveable()) {
			this.list.get(abbild[0]).draw(g);
		}
		if (this.isUpMoveable()) {
			this.list.get(abbild[1]).draw(g);
		}
		if (this.maxWidth == 1) {
			for (InterfaceTextButton button : this.list.values()) {
				this.maxWidth = Math.max(this.maxWidth, button.getNeededSize()
						.getXRange());
			}
		}
	}

	public Vector getPositionFor(InterfacePart object) {
		if (this.invertList.containsKey(object)) {
			int listPos = this.invertList.get(object);
			if (listPos != DOWN_POS && listPos != UP_POS) {
				if (listPos < this.curPos) {
					return new Vector(0.0f, -InterfaceTextButton.getSize() * 10);
				}
				if (listPos >= this.curPos + this.numberOfButtonsDisplayed) {
					return new Vector(0.0f, -InterfaceTextButton.getSize() * 10);
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
		if (listPos + this.curPos == DOWN_POS) {
			listPos = this.numberOfButtonsDisplayed;
		} else if (listPos + this.curPos == UP_POS) {
			listPos = -1;
		}
		return new Vector(0.0f,
				(InterfaceTextButton.getSize() + this.distanceBetweenButtons)
						* (listPos + 1));

	}

	private void moveUp() {
		if (this.curPos > 0) {
			this.curPos--;
		}
	}

	private void moveDown() {
		if (this.curPos < this.list.keySet().toArray().length - 2
				- numberOfButtonsDisplayed) {
			this.curPos++;
		}
	}

	private boolean isDownMoveable() {
		return this.list.keySet().toArray().length - 2 > numberOfButtonsDisplayed
				+ curPos;
	}

	private boolean isUpMoveable() {
		return this.curPos > 0;
	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		if (object.getStatus() == InterfaceObject.STATUS_PRESSED) {
			if (object.getFunction() == InterfaceFunctions.INTERFACE_TEXTBUTTONLIST_DOWN) {
				this.moveDown();
			} else if (object.getFunction() == InterfaceFunctions.INTERFACE_TEXTBUTTONLIST_UP) {
				this.moveUp();
			}
		}
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		// Nichts tun
	}

	@Override
	public void objectIsSelected(InterfaceObject object) {
		// Nichts tun
	}

	@Override
	public Rectangle getWantedSize() {
		float height = (InterfaceTextButton.getSize() + this.distanceBetweenButtons)
				* (this.numberOfButtonsDisplayed + 2)
				- this.distanceBetweenButtons;
		return new Rectangle(Vector.ZERO, this.maxWidth, height);
	}

}
