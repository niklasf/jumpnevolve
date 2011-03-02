/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine Klasse für ein Dialog-Feld, das NumberSelections und Textfelder auf
 * schwarzem Hintergrund darstellt
 * 
 * Kann ein und ausgeblendet werden
 * 
 * @author e.wagner
 * 
 */
public class Dialog extends InterfaceContainer {

	private GridContainer curCon;
	private ArrayList<DialogPart> contents;
	private boolean shown = false;

	/**
	 * 
	 */
	public Dialog() {
		GridContainer gridContainer = new GridContainer(0, 0);
		this.add(gridContainer, Vector.ZERO);
		this.curCon = gridContainer;
	}

	public void show() {
		this.setShown(true);
	}

	public void hide() {
		this.setShown(false);
	}

	public void setShown(boolean shown) {
		this.shown = shown;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> getContents() {
		HashMap<String, String> re = new HashMap<String, String>();
		for (DialogPart part : (ArrayList<DialogPart>) this.contents.clone()) {
			re.put(part.name, part.part.getContent());
		}
		return re;
	}

	private void changeCon(GridContainer gridContainer) {
		this.remove(curCon);
		this.add(gridContainer, Vector.ZERO);
		this.curCon = gridContainer;
	}

	private void addPart(DialogPart dialogPart) {
		this.contents.add(dialogPart);
		GridContainer con = new GridContainer(contents.size(), 2);
		for (int i = 0; i < this.contents.size(); i++) {
			con.add(new InterfaceLabel(this.contents.get(i).name, 12), i, 0,
					GridContainer.MODUS_X_LEFT, GridContainer.MODUS_DEFAULT);
			con.add(this.contents.get(i).part, i, 1,
					GridContainer.MODUS_X_RIGHT, GridContainer.MODUS_DEFAULT);
		}
		this.changeCon(con);
	}

	public void addNumberSelection(String name, int min, int max) {
		this.addPart(new DialogPart(new InterfaceNumberSelection(), name));
	}

	public void addTextField(String name) {
		this.addPart(new DialogPart(new InterfaceTextField(
				InterfaceFunctions.ERROR), name));
	}

	@Override
	public void poll(Input input, float secounds) {
		if (this.shown) {
			super.poll(input, secounds);
		}
	}

	@Override
	public void draw(Graphics g) {
		if (this.shown) {
			Rectangle rect = (Rectangle) this.getPreferedSize();
			Vector center = this.parentContainer.getPositionFor(this).modifyX(
					rect.width / 2).modifyY(rect.height / 2);
			Color c = g.getColor();
			// TODO: fill-Methode in GraphicsUtils auslagern
			g.setColor(Color.black);
			g.fill(rect.modifyCenter(center).toSlickShape());
			g.setColor(c);
			super.draw(g);
		}
	}

	@Override
	public Shape getPreferedSize() {
		float width = 0, height = 0;
		for (DialogPart part : this.contents) {
			width = Math.max(part.part.getPreferedSize().getXRange(), width);
			height += part.part.getPreferedSize().getYRange();
		}
		return new Rectangle(Vector.ZERO, width, height);
	}

	/**
	 * 
	 * @param name
	 * @return <code>null</code>, wenn es kein Objekt mit dem Namen gibt
	 */
	public Contentable get(String name) {
		for (DialogPart part : this.contents) {
			if (part.name.equals(name)) {
				return part.part;
			}
		}
		System.out.println("Item not found");
		return null;
	}

}
