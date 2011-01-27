/**
 * 
 */
package com.googlecode.jumpnevolve.editor2;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.Interfaceable;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class Editor2 extends Level implements Interfaceable {

	private ArrayList<EditorObject> objects = new ArrayList<EditorObject>();
	private EditorObject selected;
	private Vector cameraPos;

	/**
	 * @param loader
	 * @param width
	 * @param height
	 * @param subareaWidth
	 */
	public Editor2(Levelloader loader, int width, int height, int subareaWidth) {
		super(loader, width, height, subareaWidth);
		// TODO Auto-generated constructor stub
	}

	private void addObject(InterfaceFunction function) {

	}

	private Vector translateMousePos(float x, float y) {
		x = x - (float) this.getWidth() / 2;
		y = y - (float) this.getHeight() / 2;
		x = x / this.getZoomX();
		y = y / this.getZoomY();
		x = x + this.getCameraPosX();
		y = y + this.getCameraPosY();
		return new Vector(x, y);
	}

	private float getCameraPosY() {
		return cameraPos.y;
	}

	private float getCameraPosX() {
		return cameraPos.x;
	}

	private Vector translateMousePos(Vector mousePos) {
		return this.translateMousePos(mousePos.x, mousePos.y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void poll(Input input, float secounds) {
		this.selected.poll(input, secounds);
		Vector mousePos = new Vector(input.getMouseX(), input.getMouseY());
		Vector translatedMousePos = this.translateMousePos(mousePos);
		if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
			for (EditorObject obj : (ArrayList<EditorObject>) this.objects
					.clone()) {
				obj.isPointIn(translatedMousePos);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		for (EditorObject obj : (ArrayList<EditorObject>) this.objects.clone()) {
			obj.draw(g);
		}
		this.selected.drawInterface(g);
	}

	@Override
	public int getHeight() {
		return Engine.getInstance().getHeight();
	}

	@Override
	public int getWidth() {
		return Engine.getInstance().getWidth();
	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		// TODO Auto-generated method stub

	}
}
