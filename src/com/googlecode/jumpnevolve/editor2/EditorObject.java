package com.googlecode.jumpnevolve.editor2;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.GameObjects;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class EditorObject implements Pollable, Drawable {

	private PositionMarker position;
	private ArrayList<EditorArgument> arguments = new ArrayList<EditorArgument>();

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
		this.position = new PositionMarker(this, PositionMarker.MODUS_BOTH,
				startPosition, Color.red);
	}

	public void addArgument(EditorArgument toAdd) {
		if (!this.arguments.contains(toAdd)) {
			this.arguments.add(toAdd);
		}
	}

	public boolean isPointIn(Vector point) {
		return this.getObject().getShape().isPointIn(point);
	}

	public Vector getPosition() {
		return position.getPosition();
	}

	public AbstractObject getObject() {
		return GameObjects.loadObject(this.getDataLine(), this.parent);

	}

	public String getDataLine() {
		return this.className + "_" + this.getPosition() + "_"
				+ this.objectName + "_" + this.getActivatings() + "_"
				+ this.getArgumentString();
	}

	private String getArgumentString() {
		String re = "";
		for (int i = 0; i < this.arguments.size() - 1; i++) {
			re += this.arguments.get(i).getArgumentPart() + ",";
		}
		re += this.arguments.get(this.arguments.size() - 1).getArgumentPart();
		return re;
	}

	private String getActivatings() {
		return "none";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void poll(Input input, float secounds) {
		for (EditorArgument arg : (ArrayList<EditorArgument>) this.arguments
				.clone()) {
			arg.poll(input, secounds);
		}
	}

	@Override
	public void draw(Graphics g) {
		this.getObject().draw(g);
	}

	@SuppressWarnings("unchecked")
	public void drawInterface(Graphics g) {
		for (EditorArgument arg : (ArrayList<EditorArgument>) this.arguments
				.clone()) {
			arg.draw(g);
		}
	}
}
