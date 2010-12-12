package com.googlecode.jumpnevolve.util;

import java.awt.Image;
import java.awt.Toolkit;

import org.newdawn.slick.geom.Shape;

public class WrappedContext extends org.newdawn.slick.Graphics {

	java.awt.Graphics realGraphics;
	java.awt.image.ImageObserver observer;

	public WrappedContext(java.awt.Graphics g,
			java.awt.image.ImageObserver observer) {
		this.realGraphics = g;
		this.observer = observer;
	}

	@Override
	public void drawOval(float x, float y, float width, float height) {
		this.realGraphics.drawOval((int) x, (int) y, (int) width, (int) height);
	}

	@Override
	public void drawRect(float x, float y, float width, float height) {
		this.realGraphics.drawRect((int) x, (int) y, (int) width, (int) height);
	}

	@Override
	public void drawLine(float x, float y, float width, float height) {
		this.realGraphics.drawLine((int) x, (int) y, (int) width, (int) height);
	}

	@Override
	public void drawString(String text, float x, float y) {
		this.realGraphics.drawString(text, (int) x, (int) y);
	}

	@Override
	public void drawImage(org.newdawn.slick.Image image, float x, float y,
			float x2, float y2, float srcx, float srcy, float srcx2, float srcy2) {
		this.realGraphics.drawImage(image, (int) x, (int) y, (int) x2,
				(int) y2, (int) srcx, (int) srcy, (int) srcx2, (int) srcy2,
				this.observer); // FIXME: fixen
	}

	@Override
	public void texture(Shape shape, org.newdawn.slick.Image image, boolean fit) {
		// FIXME: Methodeninhalt einfügen
	}
}
