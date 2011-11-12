/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui.container;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.gui.Contentable;
import com.googlecode.jumpnevolve.graphics.gui.DialogPart;
import com.googlecode.jumpnevolve.graphics.gui.Informable;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceLabel;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceNumberSelection;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceTextButton;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceTextField;
import com.googlecode.jumpnevolve.math.Rectangle;
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
	private ArrayList<InterfaceTextButton> buttons = new ArrayList<InterfaceTextButton>();
	private boolean shown = false;
	private Rectangle dimensions = new Rectangle(Vector.ZERO, Vector.DOWN_LEFT);

	private boolean recalculated = false;

	/**
	 * 
	 */
	public Dialog() {
		this.enableBackground();
		this.setBackgroundColor(Color.darkGray);
		InterfaceTextButton closeButton = new InterfaceTextButton(
				InterfaceFunctions.DIALOG_CLOSE, "Dialog schließen");
		closeButton.addInformable(this);
		this.buttons.add(closeButton);
		this.updateCon();
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
		// Zusammengezählte Zahl aus Contentables und Buttons abzüglich des
		// closeButtons
		return this.contents.size() + this.buttons.size() - 1;
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

	public void addPart(DialogPart dialogPart) {
		this.contents.add(dialogPart);
		this.updateCon();
	}

	private void updateCon() {
		GridContainer con = new GridContainer(this.contents.size()
				+ this.buttons.size(), 2);
		for (int i = 0; i < this.contents.size(); i++) {
			con.add(new InterfaceLabel(this.contents.get(i).name, 12), i, 0,
					GridContainer.MODUS_X_LEFT, GridContainer.MODUS_DEFAULT);
			con.add(this.contents.get(i).part, i, 1,
					GridContainer.MODUS_X_LEFT, GridContainer.MODUS_DEFAULT);
		}
		for (int i = 0; i < this.buttons.size() - 1; i++) {
			con.add(this.buttons.get(i), i + this.contents.size(), 0,
					GridContainer.MODUS_X_LEFT, GridContainer.MODUS_DEFAULT);
		}
		con.add(this.buttons.get(this.buttons.size() - 1), this.buttons.size()
				+ this.contents.size() - 1, 1, GridContainer.MODUS_X_LEFT,
				GridContainer.MODUS_DEFAULT);
		this.calculateDimensions();
		this.changeCon(con);
	}

	private void changeCon(GridContainer gridContainer) {
		this.remove(curCon);
		this.add(gridContainer, Vector.ZERO);
		this.curCon = gridContainer;
	}

	private void calculateDimensions() {
		float height = 0, width = 0;
		for (DialogPart part : this.contents) {
			width = Math.max(part.part.getNeededSize().getXRange(), width);
			width = Math.max(new InterfaceLabel(part.name, 12).getNeededSize()
					.getXRange(), width);
			height = Math.max(part.part.getNeededSize().getYRange(), height);
		}
		for (InterfaceTextButton button : this.buttons) {
			width = Math.max(button.getNeededSize().getXRange(), width);
			height = Math.max(button.getNeededSize().getYRange(), height);
		}
		this.dimensions = new Rectangle(Vector.ZERO, width * 2, height
				* (this.contents.size() + this.buttons.size()));
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

	public void addTextButton(InterfaceFunction function, String text) {
		this.buttons.add(this.buttons.size() - 1, new InterfaceTextButton(
				function, text));
		this.updateCon();
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
			super.draw(g);
			if (!this.recalculated) {
				this.calculateDimensions();
				this.recalculated = true;
			}
		}
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

	/**
	 * Deaktiviert alle Dialog (blendet sie aus)
	 */
	public static void disableAll() {
		for (Dialog dialog : Instances) {
			dialog.hide();
		}
	}

	@Override
	public Rectangle getWantedSize() {
		return this.dimensions;
	}

}
