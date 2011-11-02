package com.googlecode.jumpnevolve.graphics.gui.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.gui.Contentable;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

public class InterfaceCheckbox extends InterfaceObject implements Contentable {

	private static final float SIZE = Parameter.GUI_CHECKBOX_SIZE;

	private boolean value = false;
	private Rectangle shape = new Rectangle(Vector.ZERO, SIZE, SIZE);

	public InterfaceCheckbox(InterfaceFunction function, boolean startValue) {
		super(function);
		this.value = startValue;
	}

	@Override
	public Rectangle getNeededSize() {
		return this.shape;
	}

	@Override
	public void draw(Graphics g) {
		if (this.value) {
			GraphicUtils.fill(g,
					this.shape.modifyCenter(this.getCenterVector()));
		} else {
			GraphicUtils.draw(g,
					this.shape.modifyCenter(this.getCenterVector()));
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
