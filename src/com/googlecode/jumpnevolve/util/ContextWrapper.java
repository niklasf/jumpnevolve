package com.googlecode.jumpnevolve.util;

public class ContextWrapper {

	public static org.newdawn.slick.Graphics wrap(java.awt.Graphics g,
			java.awt.image.ImageObserver observer) {
		return new WrappedContext(g, observer);
	}
}
