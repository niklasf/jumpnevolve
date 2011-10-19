/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

	private static ArrayList<Dialog> Instances = new ArrayList<Dialog>();

	private GridContainer curCon;
	private ArrayList<DialogPart> contents = new ArrayList<DialogPart>();
	private boolean shown = false;
	private InterfaceTextButton closeButton;
	private Rectangle dimensions = new Rectangle(Vector.ZERO, Vector.DOWN_LEFT);

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
		Dialog.Instances.add(this);
	}

	public static boolean isAnyDialogActive() {
		for (Dialog obj : Dialog.Instances) {
			if (obj.isActive()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return Gibt zurück, ob der Dialog aktiv ist (entspricht, ob er angezeigt
	 *         ist)
	 */
	public boolean isActive() {
		return this.shown;
	}

	public void show() {
		this.setShown(true);
	}

	public void hide() {
		this.setShown(false);
	}

	public void switchStatus() {
		this.setShown(!this.shown);
	}

	public void setShown(boolean shown) {
		if (this.getNumberOfParts() > 0) {
			this.shown = shown;
		} else {
			this.shown = false;
		}
	}

	public int getNumberOfParts() {
		return this.contents.size();
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

	public void addPart(DialogPart dialogPart) {
		this.contents.add(dialogPart);
		GridContainer con = new GridContainer(contents.size() + 1, 2);
		for (int i = 0; i < this.contents.size(); i++) {
			con.add(new InterfaceLabel(this.contents.get(i).name, 12), i, 0,
					GridContainer.MODUS_X_LEFT, GridContainer.MODUS_DEFAULT);
			con.add(this.contents.get(i).part, i, 1,
					GridContainer.MODUS_X_LEFT, GridContainer.MODUS_DEFAULT);
		}
		con.add(this.closeButton, this.contents.size(), 1);
		this.calculateDimensions();
		this.changeCon(con);
	}

	private void calculateDimensions() {
		// TODO: Noch fehlerhaft, wenn als Parts nur Textfelder verwendet werden
		// und die Labels lang sind --> new InterfaceLabel(part.name,
		// 12).getNeededSize().getXRange() liefert noch ein falsches Ergebnis,
		// aufgrund einer falschen Schriftart --> Behebung durch
		// Implementierungen eigener Schriftarten, die korrekt angezeigt werden
		float widthParts = 0, widthLabels = 0, height = 0;
		for (DialogPart part : this.contents) {
			widthParts = Math.max(part.part.getNeededSize().getXRange(),
					widthParts);
			widthLabels = Math.max(new InterfaceLabel(part.name, 12)
					.getNeededSize().getXRange(), widthLabels);
			height = Math.max(part.part.getNeededSize().getYRange(), height);
		}
		height = Math.max(this.closeButton.getNeededSize().getYRange(), height);
		float width = Math.max(widthParts, widthLabels);
		this.dimensions = new Rectangle(Vector.ZERO, width * 2, height
				* this.contents.size());
	}

	/**
	 * Fügt dem Dialog ein Contentable hinzu
	 * 
	 * @param part
	 *            Das Contentable
	 * @param name
	 *            Die Bezeichnung, unter der das Contentable abgelegt wird
	 */
	public void addContentable(Contentable part, String name) {
		this.addPart(new DialogPart(part, name));
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
				InterfaceFunctions.INTERFACE_TEXTFIELD), name));
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
			g.setColor(Color.darkGray);
			g.fill(rect.modifyCenter(center).toSlickShape());
			g.setColor(c);
			super.draw(g);
		}
	}

	@Override
	public Shape getNeededSize() {
		return this.dimensions;
	}

	/**
	 * 
	 * @param name
	 * @return <code>null</code>, wenn es kein Objekt mit dem Namen gibt
	 */
	@SuppressWarnings("unchecked")
	public Contentable getContentable(String name) {
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

	@Override
	public void objectIsSelected(InterfaceObject object) {
		// Nichts tun
	}

	public Rectangle getPlaceFor(InterfacePart object) {
		if (object == this.curCon) {
			return (Rectangle) this.getNeededSize();
		} else {
			return super.getPlaceFor(object);
		}
	}

}
