package com.googlecode.jumpnevolve.graphics.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

public class InterfaceCheckbox extends InterfaceObject implements Contentable {

	private static final float SIZE = 20.0f;

	private boolean value = false;
	private Shape shape = new Rectangle(Vector.ZERO, SIZE, SIZE);

	public InterfaceCheckbox(InterfaceFunction function, boolean startValue) {
		super(function);
		this.value = startValue;
	}

	@Override
	public Shape getNeededSize() {
		return this.shape;
	}

	@Override
	public void draw(Graphics g) {
		if (this.value) {
			GraphicUtils.fill(g,
					this.shape.modifyCenter(this.getTransformedCenterVector()));
		} else {
			GraphicUtils.draw(g,
					this.shape.modifyCenter(this.getTransformedCenterVector()));
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		if (this.getStatus() == STATUS_PRESSED) {
			this.value = !this.value;
		}
	}

	@Override
	public String getContent() {
		return "" + this.value;
	}

	@Override
	public void setContent(String newContent) {
		this.value = Boolean.parseBoolean(newContent);
	}

}
