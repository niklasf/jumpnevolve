package com.googlecode.jumpnevolve.game.campaign;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

public class LevelMarker implements Drawable {

	public static final int STATUS_FINISHED = 0;
	public static final int STATUS_AVAIABLE = 1;
	public static final int STATUS_NOTAVAIBLE = 2;
	public final String name;
	private int status;
	public final Vector position;
	private HashMap<Vector, LevelMarker> connections = new HashMap<Vector, LevelMarker>();

	public LevelMarker(String name, Vector position, int status) {
		this.name = name;
		this.status = status;
		this.position = position;
	}

	public LevelMarker(String source, Vector position) {
		this(source, position, STATUS_NOTAVAIBLE);
	}

	public void setStatus(int status) {
		this.status = status;
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

	public void addConnection(LevelConnection levelConnection) {
		if (levelConnection.pos1 == this) {
			this.connections.put(
					levelConnection.pos2.position.sub(this.position),
					levelConnection.pos2);
		} else {
			this.connections.put(
					levelConnection.pos1.position.sub(this.position),
					levelConnection.pos1);
		}
	}

	public Collection<LevelMarker> getConnections() {
		return this.connections.values();
	}

	public int getStatus() {
		return this.status;
	}

	public LevelMarker getLevelInDirection(Vector direction) {
		float minAng = Float.POSITIVE_INFINITY;
		Vector cur = null;
		for (Vector vec : this.connections.keySet()) {
			float ang = vec.ang(direction);
			if (ang < minAng) {
				cur = vec;
				minAng = ang;
			}
		}
		return this.connections.get(cur);
	}
}
