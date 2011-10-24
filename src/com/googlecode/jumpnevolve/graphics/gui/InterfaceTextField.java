package com.googlecode.jumpnevolve.graphics.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.math.PointLine;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Textfeld für das Interface
 * 
 * @author Erik Wagner
 * 
 */
public class InterfaceTextField extends InterfaceObject implements Contentable {

	private static final float DELAY_LENGTH = 0.15f;
	private Timer input_timer = new Timer(DELAY_LENGTH);
	private String content1 = "", content2 = "";
	private static Font font;

	public InterfaceTextField(InterfaceFunction function) {
		super(function);
	}

	@Override
	public Shape getNeededSize() {
		if (font == null) {
			float width = this.getContent().length() * 10;
			if (width < 50) {
				width = 50;
			}
			return new Rectangle(Vector.ZERO, width, 20);
		} else {
			return new Rectangle(Vector.ZERO, font.getWidth(this.getContent()),
					font.getLineHeight());
		}
	}

	@Override
	public void draw(Graphics g) {
		if (font == null) {
			font = g.getFont();
		}
		Rectangle rect = (Rectangle) this.getNeededSize();
		Vector center = this.getTransformedCenterVector();

		GraphicUtils.fill(g, rect.modifyCenter(center), Color.blue);

		Vector pos = this.parent.getTransformedPositionFor(this);
		GraphicUtils.drawString(g, pos.add(2, 0), this.content1);

		float xModifier = g.getFont().getWidth(this.content1);
		GraphicUtils.drawString(g, pos.add(xModifier + 5, 0), this.content2);
		GraphicUtils.draw(
				g,
				new PointLine(pos.add(xModifier + 3, 3), pos.add(xModifier + 3,
						15)), Color.white);

		GraphicUtils.draw(g, this.getNeededSize().getBoundingRect()
				.modifyCenter(center), Color.white);
	}

	@Override
	public String getContent() {
		return this.content1 + this.content2;
	}

	@Override
	public void setContent(String newContent) {
		if (newContent.length() > this.content1.length()) {
			this.content1 = newContent.substring(0, this.content1.length());
			this.content2 = newContent.substring(this.content1.length());
		} else {
			this.content1 = newContent;
			this.content2 = "";
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		if (this.isSelected()) {
			if (input.isKeyDown(Input.KEY_BACK)) {
				if (!this.input_timer.isRunning()) {
					this.input_timer.start(DELAY_LENGTH);
					if (this.content1.length() >= 1) {
						this.content1 = this.content1.substring(0,
								this.content1.length() - 1);
					}
				}
			} else if (input.isKeyDown(Input.KEY_DELETE)) {
				if (!this.input_timer.isRunning()) {
					this.input_timer.start(DELAY_LENGTH);
					if (this.content2.length() >= 1) {
						this.content2 = this.content2.substring(1);
					}
				}
			} else if (input.isKeyDown(Input.KEY_LEFT)) {
				if (!this.input_timer.isRunning()) {
					this.input_timer.start(DELAY_LENGTH);
					if (this.content1.length() >= 1) {
						this.content2 = this.content1.charAt(this.content1
								.length() - 1) + this.content2;
						this.content1 = this.content1.substring(0,
								this.content1.length() - 1);
					}
				}
			} else if (input.isKeyDown(Input.KEY_RIGHT)) {
				if (!this.input_timer.isRunning()) {
					this.input_timer.start(DELAY_LENGTH);
					if (this.content2.length() >= 1) {
						this.content1 = this.content1 + this.content2.charAt(0);
						this.content2 = this.content2.substring(1);
					}
				}
			} else {
				if (!this.input_timer.isRunning()) {
					for (int i = 0; i < 255; i++) {
						if (input.isKeyDown(i)) {
							String keyName = Input.getKeyName(i).toLowerCase();
							if (keyName.length() == 1) {
								this.content1 += keyName;
								this.input_timer.start(DELAY_LENGTH);
							} else {
								if (keyName.equals("period")) {
									this.content1 += ".";
									this.input_timer.start(DELAY_LENGTH);
								}
								if (keyName.equals("minus")) {
									this.content1 += "-";
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
