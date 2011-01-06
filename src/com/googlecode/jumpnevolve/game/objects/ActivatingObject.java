package com.googlecode.jumpnevolve.game.objects;

import java.util.ArrayList;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

public abstract class ActivatingObject extends ObjectTemplate implements
		Activating {

	private static final long serialVersionUID = -1120481829739625560L;

	private ArrayList<Activable> objectsToActivate = new ArrayList<Activable>();
	private ArrayList<Activable> objectsToDeactivate = new ArrayList<Activable>();

	public ActivatingObject(World world, Shape shape, float mass,
			boolean blockable, boolean pushable) {
		super(world, shape, mass, blockable, pushable);
	}

	/**
	 * @param object
	 *            Das Objekt das durch dieses Objekt aktiviert werden soll
	 */
	public final void addActivable(Activable object) {
		if (object.isActivableBy(this)) {
			this.objectsToActivate.add(object);
		}
		if (object.isDeactivableBy(this)) {
			this.objectsToDeactivate.add(object);
		}
	}

	/**
	 * @return Die Objekte, die durch dieses Objekt aktiviert werden
	 */
	public final ArrayList<Activable> getObjectsToActivate() {
		return this.objectsToActivate;
	}

	/**
	 * @return Die Objekte, die durch dieses Objekt aktiviert werden
	 */
	public final ArrayList<Activable> getObjectsToDeactivate() {
		return this.objectsToDeactivate;
	}

	@Override
	public int getCompany() {
		return COMPANY_OBJECT;
	}
}
