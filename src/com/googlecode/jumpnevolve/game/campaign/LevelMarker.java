package com.googlecode.jumpnevolve.game.campaign;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

public class LevelMarker implements Drawable {

	private static final int STATUS_FINISHED = 0;
	private static final int STATUS_AVAIABLE = 1;
	private static final int STATUS_NOTAVAIBLE = 2;
	public final String levelSource;
	private int status;
	private Vector position;

	public LevelMarker(String source, Vector position, int status) {
		this.levelSource = source;
		this.status = status;
		this.position = position;
	}

	public LevelMarker(String source, Vector position) {
		this(source, position, STATUS_NOTAVAIBLE);
	}

	@Override
	public void draw(Graphics g) {
		// TODO: Anstatt einfachen Kreisen könnten Grafiken mit Lichtreflex etc.
		// verwendet werden
		switch (status) {
		case STATUS_FINISHED:
			GraphicUtils.draw(g, new Circle(this.position,
					Parameter.CAMPAIGN_MARKER_RADIUS), Color.green);
			break;
		case STATUS_AVAIABLE:
			GraphicUtils.draw(g, new Circle(this.position,
					Parameter.CAMPAIGN_MARKER_RADIUS), Color.blue);
			break;
		case STATUS_NOTAVAIBLE:
		default:
			GraphicUtils.draw(g, new Circle(this.position,
					Parameter.CAMPAIGN_MARKER_RADIUS), Color.red);
			break;
		}
	}
}
