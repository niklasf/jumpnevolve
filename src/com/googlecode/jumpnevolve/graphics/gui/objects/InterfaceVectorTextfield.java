package com.googlecode.jumpnevolve.graphics.gui.objects;

import java.util.ArrayList;

import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.graphics.gui.ContentListener;
import com.googlecode.jumpnevolve.graphics.gui.Contentable;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.container.GridContainer;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

public class InterfaceVectorTextfield extends GridContainer implements
		Contentable, ContentListener {

	private InterfaceTextField xField, yField;

	private ArrayList<ContentListener> listener = new ArrayList<ContentListener>();

	public InterfaceVectorTextfield() {
		super(2, 2);
		this.xField = new InterfaceTextField(
				InterfaceFunctions.INTERFACE_TEXTFIELD_VECTOR_X);
		this.yField = new InterfaceTextField(
				InterfaceFunctions.INTERFACE_TEXTFIELD_VECTOR_Y);
		this.add(new InterfaceLabel("X:", 12), 0, 0, MODUS_X_LEFT,
				MODUS_DEFAULT);
		this.add(new InterfaceLabel("Y:", 12), 1, 0, MODUS_X_LEFT,
				MODUS_DEFAULT);
		this.add(this.xField, 0, 1, MODUS_X_RIGHT, MODUS_DEFAULT);
		this.add(this.yField, 1, 1, MODUS_X_RIGHT, MODUS_DEFAULT);
		this.xField.addContentListener(this);
		this.yField.addContentListener(this);
	}

	@Override
	public Rectangle getWantedSize() {
		Rectangle xSize = this.xField.getNeededSize();
		return new Rectangle(Vector.ZERO, Math.max(xSize.width,
				this.yField.getNeededSize().width) + 20, 2 * xSize.height + 5);
	}

	@Override
	public String getContent() {
		return this.xField.getContent() + "|" + this.yField.getContent();
	}

	@Override
	public void setContent(String newContent) {
		try {
			Vector content = Vector.parseVector(newContent);
			this.xField.setContent("" + content.x);
			this.yField.setContent("" + content.y);
		} catch (NumberFormatException e) {
			Log.warn("VectorTextfield: Ungültiger Content: " + newContent);
		}
	}

	@Override
	public void addContentListener(ContentListener listener) {
		this.listener.add(listener);
	}

	@Override
	public void contentChanged(Contentable object) {
		for (ContentListener cL : this.listener) {
			cL.contentChanged(this);
		}
	}
}
