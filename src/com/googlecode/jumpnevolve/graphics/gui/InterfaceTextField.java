package com.googlecode.jumpnevolve.graphics.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

//FIXME: Klasse mit Inhalt füllen
public class InterfaceTextField extends InterfaceObject implements Contentable {

	private Timer input_timer = new Timer(0.1f);
	private String content = "";

	public InterfaceTextField(InterfaceFunction function) {
		super(function);
	}

	@Override
	public Shape getPreferedSize() {
		return new Rectangle(Vector.ZERO, this.content.length() * 10 + 4, 14);
	}

	@Override
	public void draw(Graphics g) {
		Vector pos = this.parent.getPositionFor(this);
		GraphicUtils.drawString(g, pos.add(2, 2), this.content);
		Color c = g.getColor();
		g.setColor(Color.white);
		GraphicUtils.draw(g, this.getPreferedSize().getBoundingRect()
				.modifyCenter(this.getCenterVector()));
		g.setColor(c);
	}

	@Override
	public String getContent() {
		return this.content;
	}

	@Override
	public void setContent(String newContent) {
		this.content = newContent;
	}

	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		if (input.isKeyDown(Input.KEY_BACK)) {
			if (!this.input_timer.isRunning()) {
				this.input_timer.start(0.1f);
				this.content = this.content.substring(0,
						this.content.length() - 2);
			}
		} else {
			if (!this.input_timer.isRunning()
					&& this.getStatus() != STATUS_NOTHING) {
				for (int i = 0; i < 224; i++) {
					if (input.isKeyDown(i)) {
						this.content += Input.getKeyName(i);
						this.input_timer.start(0.1f);
					}
				}
			}
		}
	}
}
