package com.googlecode.jumpnevolve.editor;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.editor.arguments.EditorArgument;
import com.googlecode.jumpnevolve.editor.arguments.PositionMarker;
import com.googlecode.jumpnevolve.game.GameObjects;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.gui.container.Dialog;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.ObjectGroup;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 *         TODO: Im Dialog sollten die Vektor exakt einstellbar sein
 */
public class EditorObject implements Pollable, Drawable {

	private PositionMarker newPosition;
	private ArrayList<EditorArgument> newArguments = new ArrayList<EditorArgument>();
	private AbstractObject object = null;
	private String lastDataLine = "";

	public Dialog settings = new Dialog();
	public final Editor2 parent;
	public final String objectName, className;

	/**
	 *
	 */
	public EditorObject(Editor2 parent, String objectName, String className,
			Vector startPosition) {
		this.parent = parent;
		this.objectName = objectName;
		this.className = className;
		this.newPosition = new PositionMarker(parent, "Position",
				PositionMarker.MODUS_BOTH, startPosition, Color.red);
		this.settings.addPart(this.newPosition.getDialogPart());
		if (GameObjects.getGameObject(className).hasActivatings) {
			// TODO: Activatings sollten auch über den Editor direkt ausgewählt
			// werden können --> Button, der gedrückt wird, danach legt
			// Rechtsklick Activating fest
			this.settings.addTextField("Activatings");
		}
	}

	public void initialize(String activatings, String argumentString) {
		if (GameObjects.getGameObject(className).hasActivatings) {
			this.settings.getContentable("Activatings").setContent(activatings);
		}
		String[] split = argumentString.split(",");
		for (int i = 0; i < this.newArguments.size(); i++) {
			this.newArguments.get(i).initialize(split[i]);
		}
	}

	public void addNewArgument(EditorArgument toAdd) {
		if (!this.newArguments.contains(toAdd)) {
			this.newArguments.add(toAdd);
			this.settings.addPart(toAdd.getDialogPart());
		}
	}

	public boolean isPointIn(Vector point) {
		return this.getObject().getShape().isPointIn(point);
	}

	public Vector getPosition() {
		return this.newPosition.getPosition();
	}

	public EditorArgument getPositionMarker() {
		return this.newPosition;
	}

	public AbstractObject getObject() {
		String line = this.getDataLine();
		if (!this.lastDataLine.equals(line)) {
			this.object = GameObjects.loadObject(line, this.parent);
			this.lastDataLine = line;
		}
		return this.object;
	}

	public String getDataLine() {
		return this.className + "_" + this.getPosition() + "_"
				+ this.objectName + "_" + this.getActivatings() + "_"
				+ this.getArgumentString();
	}

	public void showDialog() {
		this.settings.show();
	}

	public void hideDialog() {
		this.settings.hide();
	}

	public void switchDialogStatus() {
		this.settings.switchStatus();
	}

	public boolean hasActivatins() {
		return GameObjects.getGameObject(this.className).hasActivatings;
	}

	private String getArgumentString() {
		if (this.newArguments.size() > 0) {
			String re = "";
			for (int i = 0; i < this.newArguments.size() - 1; i++) {
				re += this.newArguments.get(i).getArgumentPart() + ",";
			}
			re += this.newArguments.get(this.newArguments.size() - 1)
					.getArgumentPart();
			return re;
		} else {
			return "none";
		}
	}

	private String getActivatings() {
		if (this.hasActivatins()) {
			return this.settings.getContentable("Activatings").getContent();
		} else {
			return "none";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void poll(Input input, float secounds) {
		for (EditorArgument arg : (ArrayList<EditorArgument>) this.newArguments
				.clone()) {
			arg.poll(input, secounds);
		}
		this.settings.poll(input, secounds);
		this.newPosition.poll(input, secounds);
	}

	@Override
	public void draw(Graphics g) {
		this.drawObject(g);
	}

	private void drawObject(Graphics g) {
		// Objekt erstellen
		this.getObject();

		// Objekt zeichnen
		this.object.drawForEditor(g);

		// Bei einer Objektgruppe auch alle anderen Objekte zeichnen
		if (this.object instanceof ObjectGroup) {
			for (AbstractObject obj : ((ObjectGroup) this.object).getObjects()) {
				obj.draw(g);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void drawInterface(Graphics g) {
		// TODO: Auswahlmöglichkeit, wann der Name angezeigt werden soll (immer
		// oder nur zusammen mit dem Interface)
		GraphicUtils.drawString(g, this.getPosition(), this.objectName);
		for (EditorArgument arg : (ArrayList<EditorArgument>) this.newArguments
				.clone()) {
			arg.draw(g);
		}
		this.newPosition.draw(g);
	}
}
