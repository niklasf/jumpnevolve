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

		// TODO: Hier und in andern Klassen die Centerbestimmung ändern -->
		// Wegen der Kamera-Position treten sonst Fehler auf
		Vector pos2 = this.getCenterVector();
		pos2 = pos2.add(this.parent
				.getInterfaceable()
				.getCamera()
				.getPosition()
				.sub(new Vector(this.parent.getInterfaceable().getWidth() / 2,
						this.parent.getInterfaceable().getHeight() / 2)));

		Rectangle rect = (Rectangle) this.getPreferedSize();
		Vector center = this.parent.getPositionFor(this)
				.modifyX(rect.width / 2).modifyY(rect.height / 2).mul(10);
		center = pos2;
		Color cc = g.getColor();
		// TODO: fill-Methode in GraphicsUtils auslagern
		g.setColor(Color.blue);
		g.fill(rect.modifyCenter(center).toSlickShape());
		g.setColor(cc);

		Vector pos = this.parent.getPositionFor(this);
		GraphicUtils.drawString(g, pos, this.content);
		Color c = g.getColor();
		g.setColor(Color.white);
		GraphicUtils.draw(g, this.getPreferedSize().getBoundingRect()
				.modifyCenter(center));
		g.setColor(c);

		System.out.println("Drawed " + this.content + " " + center);
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
					this.input_timer.start(0.1f);
					this.content = this.content.substring(0,
							this.content.length() - 2);
				}
			} else {
				if (!this.input_timer.isRunning()) {
					for (int i = 0; i < 224; i++) {
						if (input.isKeyDown(i)) {
							this.content += Input.getKeyName(i);
							this.input_timer.start(0.1f);
						}
					}
				}
			}
		}
		this.input_timer.poll(input, secounds);
	}
}
