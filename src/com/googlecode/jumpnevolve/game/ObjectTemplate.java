package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.BackgroundDrawable;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Accompanying;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Die Vorlage für alle Objekte jeder Art.
 * 
 * Objekte zeichnen sich dadurch aus, dass sie sich nicht selbst bewegen und tot
 * sind. Sie können schiebbar, blockbar und auch aktivierbar und in der Lage
 * sein zu töten.
 * <p>
 * Objekte werden im Hintergrund gezeichnet.
 * 
 * @author Erik Wagner
 * 
 */
public abstract class ObjectTemplate extends AbstractObject implements
		Accompanying, BackgroundDrawable {

	private static final long serialVersionUID = 2329577111424159238L;

	public ObjectTemplate(World world, NextShape shape, float mass) {
		super(world, shape, mass);
	}

	public ObjectTemplate(World world, NextShape shape, float mass,
			Vector velocity) {
		super(world, shape, mass, velocity);
	}

	@Override
	public int getCompany() {
		return COMPANY_OBJECT;
	}
}
