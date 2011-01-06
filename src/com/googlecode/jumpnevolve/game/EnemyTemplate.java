package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Fighting;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

/**
 * Die Vorlage für jede Art von Gegnern.
 * 
 * Ein Gegner zeichnet sich dadurch aus, dass er lebt und töten kann. Er kann
 * nicht geschoben oder aktiviert werden. Er kann blockbar sein, muss es aber
 * nicht.
 * 
 * Gegner töten normalerweise alle lebenden Objekte auf die sie stoßen bis auf
 * andere Gegner.
 * 
 * @author Erik Wagner
 * 
 */
public abstract class EnemyTemplate extends AbstractObject implements Fighting {

	private static final long serialVersionUID = 6877195164492992576L;

	public EnemyTemplate(World world, Shape shape, float mass, boolean blockable) {
		super(world, shape, mass, blockable, false);
	}

	@Override
	public int getCompany() {
		return COMPANY_ENEMY;
	}
}
