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

	private static final float DELAY_LENGTH = 0.15f;
	private Timer input_timer = new Timer(DELAY_LENGTH);
	private String content = "";

	public InterfaceTextField(InterfaceFunction function) {
		super(function);
	}

	@Override
	public Shape getNeededSize() {
		float width = this.content.length() * 10;
		if (width < 50) {
			width = 50;
		}
		return new Rectangle(Vector.ZERO, width, 20);
	}

	@Override
	public void draw(Graphics g) {

		// TODO: Hier und in andern Klassen die Centerbestimmung ändern -->
		// Wegen der Kamera-Position treten sonst Fehler auf

		Rectangle rect = (Rectangle) this.getNeededSize();
		Vector center = this.getTransformedCenterVector();
		Color cc = g.getColor();

		// TODO: fill-Methode in GraphicsUtils auslagern
		g.setColor(Color.blue);
		g.fill(rect.modifyCenter(center).toSlickShape());
		g.setColor(cc);

		Vector pos = this.parent.getTransformedPositionFor(this);
		GraphicUtils.drawString(g, pos.add(2, 0), this.content);
		Color c = g.getColor();
		g.setColor(Color.white);
		GraphicUtils.draw(g, this.getNeededSize().getBoundingRect()
				.modifyCenter(center));
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
		if (this.getStatus() != STATUS_NOTHING) {
			if (input.isKeyDown(Input.KEY_BACK)) {
				if (!this.input_timer.isRunning()) {
					this.input_timer.start(DELAY_LENGTH);
					if (this.content.length() >= 1) {
						this.content = this.content.substring(0,
								this.content.length() - 1);
					}
				}
			} else {
				if (!this.input_timer.isRunning()) {
					for (int i = 0; i < 255; i++) {
						if (input.isKeyDown(i)) {
							String keyName = Input.getKeyName(i).toLowerCase();
							if (keyName.length() == 1) {
								this.content += keyName;
								this.input_timer.start(DELAY_LENGTH);
							} else {
								if (keyName.equals("period")) {
									this.content += ".";
									this.input_timer.start(DELAY_LENGTH);
								}
							}
						}
					}
				}
			}
		}
		this.input_timer.poll(input, secounds);
	}
}
