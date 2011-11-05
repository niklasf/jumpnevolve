package com.googlecode.jumpnevolve.graphics.gui.objects;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.gui.ContentListener;
import com.googlecode.jumpnevolve.graphics.gui.Contentable;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.math.PointLine;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * Ein Textfeld für das Interface
 * 
 * @author Erik Wagner
 * 
 */
public class InterfaceTextField extends InterfaceObject implements Contentable {

	private static final float DELAY_LENGTH = Parameter.GUI_TEXTFIELD_DELAY;
	private Timer input_timer = new Timer(DELAY_LENGTH);
	private String content1 = "", content2 = "";
	private boolean writeable;
	private static Font font;

	private ArrayList<ContentListener> listener = new ArrayList<ContentListener>();

	public InterfaceTextField(InterfaceFunction function, boolean writeable) {
		super(function);
		this.writeable = writeable;
	}

	public InterfaceTextField(InterfaceFunction function) {
		this(function, true);
	}

	public void setWriteable(boolean status) {
		this.writeable = status;
	}

	@Override
	public Rectangle getNeededSize() {
		if (font == null) {
			float width = this.getContent().length() * 10;
			if (width < 50) {
				width = 50;
			}
			return new Rectangle(Vector.ZERO, width, 20);
		} else {
			return new Rectangle(Vector.ZERO,
					font.getWidth(this.getContent() + 10), font.getLineHeight());
		}
	}

	@Override
	public void draw(Graphics g) {
		if (font == null) {
			font = g.getFont();
		}
		Rectangle rect = (Rectangle) this.getNeededSize();
		Vector center = this.getCenterVector();

		GraphicUtils.fill(g, rect.modifyCenter(center), Color.blue);

		Vector pos = this.parent.getPositionFor(this);
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
		if (this.isSelected() && this.writeable) {
			if (input.isKeyDown(Input.KEY_BACK)) {
				if (!this.input_timer.isRunning()) {
					this.input_timer.start(DELAY_LENGTH);
					if (this.content1.length() >= 1) {
						this.changeContent(
								this.content1.substring(0,
										this.content1.length() - 1),
								this.content2);
					}
				}
			} else if (input.isKeyDown(Input.KEY_DELETE)) {
				if (!this.input_timer.isRunning()) {
					this.input_timer.start(DELAY_LENGTH);
					if (this.content2.length() >= 1) {
						this.changeContent(this.content1,
								this.content2.substring(1));
					}
				}
			} else if (input.isKeyDown(Input.KEY_LEFT)) {
				if (!this.input_timer.isRunning()) {
					this.input_timer.start(DELAY_LENGTH);
					if (this.content1.length() >= 1) {
						this.changeContent(
								this.content1.substring(0,
										this.content1.length() - 1),
								this.content1.charAt(this.content1.length() - 1)
										+ this.content2);
					}
				}
			} else if (input.isKeyDown(Input.KEY_RIGHT)) {
				if (!this.input_timer.isRunning()) {
					this.input_timer.start(DELAY_LENGTH);
					if (this.content2.length() >= 1) {
						this.changeContent(
								this.content1 + this.content2.charAt(0),
								this.content2.substring(1));
					}
				}
			} else {
				if (!this.input_timer.isRunning()) {
					for (int i = 0; i < 255; i++) {
						if (input.isKeyDown(i)) {
							String keyName = Input.getKeyName(i).toLowerCase();
							if (keyName.length() == 1) {
								if (input.isKeyDown(Input.KEY_LSHIFT)
										|| input.isKeyDown(Input.KEY_RSHIFT)) {
									keyName = keyName.toUpperCase();
								}
								this.changeContent(this.content1 += keyName,
										this.content2);
								this.input_timer.start(DELAY_LENGTH);
							} else {
								if (keyName.equals("period")) {
									this.changeContent(this.content1 += ".",
											this.content2);
									this.input_timer.start(DELAY_LENGTH);
								}
								if (keyName.equals("minus")) {
									this.changeContent(this.content1 += "-",
											this.content2);
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

	private void changeContent(String c1, String c2) {
		this.content1 = c1;
		this.content2 = c2;
		for (ContentListener cL : this.listener) {
			cL.contentChanged(this);
		}
	}

	@Override
	public void addContentListener(ContentListener listener) {
		this.listener.add(listener);
	}
}
