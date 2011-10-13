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
public class Dialog extends InterfaceContainer implements Informable {

	private GridContainer curCon;
	private ArrayList<DialogPart> contents = new ArrayList<DialogPart>();
	private boolean shown = false;
	private InterfaceTextButton closeButton;

	/**
	 * 
	 */
	public Dialog() {
		this.closeButton = new InterfaceTextButton(
				InterfaceFunctions.DIALOG_CLOSE, "Dialog schließen");
		this.closeButton.addInformable(this);
		GridContainer gridContainer = new GridContainer(1, 1);
		gridContainer.add(closeButton, 0, 0);
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

	/**
	 * @return Eine Hash-Map, die nach Bezeichnungen geordnet die Werte der
	 *         Dialog-Teile (als Strings) enthält
	 */
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
		GridContainer con = new GridContainer(contents.size() + 1, 2);
		for (int i = 0; i < this.contents.size(); i++) {
			con.add(new InterfaceLabel(this.contents.get(i).name, 12), i, 0,
					GridContainer.MODUS_X_LEFT, GridContainer.MODUS_DEFAULT);
			con.add(this.contents.get(i).part, i, 1,
					GridContainer.MODUS_X_LEFT, GridContainer.MODUS_DEFAULT);
		}
		con.add(this.closeButton, this.contents.size(), 1);
		this.changeCon(con);
	}

	/**
	 * Fügt dem Dialog eine Zahlenauswahl hinzu
	 * 
	 * @param name
	 *            Bezeichnung der Zahlenauswahl
	 * @param min
	 *            Der minimale Wert
	 * @param max
	 *            Der maximale Wert
	 */
	public void addNumberSelection(String name, int min, int max) {
		this.addPart(new DialogPart(new InterfaceNumberSelection(min, max),
				name));
	}

	/**
	 * Fügt dem Dialog ein Textfeld hinzu
	 * 
	 * @param name
	 *            Bezeichnung des Textfeldes
	 */
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
			Rectangle rect = (Rectangle) this.getNeededSize();
			Vector center = this.parentContainer
					.getTransformedPositionFor(this);
			center = center.modifyX(center.x + rect.width / 2).modifyY(
					center.y + rect.height / 2);
			Color c = g.getColor();
			// TODO: fill-Methode in GraphicsUtils auslagern
			g.setColor(Color.red);
			g.fill(rect.modifyCenter(center).toSlickShape());
			g.setColor(c);
			super.draw(g);
		}
	}

	@Override
	public Shape getNeededSize() {
		float width = 0, height = 0;
		for (DialogPart part : this.contents) {
			width = Math.max(part.part.getNeededSize().getXRange(), width);
			height += part.part.getNeededSize().getYRange();
		}
		// return new Rectangle(Vector.ZERO, width * 2, height);
		return new Rectangle(Vector.ZERO, 400, 400);
	}

	/**
	 * 
	 * @param name
	 * @return <code>null</code>, wenn es kein Objekt mit dem Namen gibt
	 */
	@SuppressWarnings("unchecked")
	public Contentable get(String name) {
		for (DialogPart part : (ArrayList<DialogPart>) this.contents.clone()) {
			if (part.name.equals(name)) {
				return part.part;
			}
		}
		System.out.println("Item not found");
		return null;
	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		if (object.function == InterfaceFunctions.DIALOG_CLOSE) {
			this.hide();
		}
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		// Nichts tun
	}

}
