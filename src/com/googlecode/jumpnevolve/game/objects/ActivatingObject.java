package com.googlecode.jumpnevolve.game.objects;

import java.util.ArrayList;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

public abstract class ActivatingObject extends ObjectTemplate {

	private ArrayList<AbstractObject> objectsToActivate = new ArrayList<AbstractObject>();

	public ActivatingObject(World world, Shape shape, float mass,
			boolean blockable, boolean pushable, boolean activable,
			boolean killable) {
		super(world, shape, mass, blockable, pushable, activable, killable);
	}

	/**
	 * @param object
	 *            Das Objekt das durch dieses Objekt aktiviert werden soll
	 */
	public final void addObjectsToActivate(AbstractObject object) {
		this.objectsToActivate.add(object);
	}

	/**
	 * @return Die Objekte, die durch dieses Objekt aktiviert werden
	 */
	public final ArrayList<AbstractObject> getObjectsToActivate() {
		return this.objectsToActivate;
	}
}
