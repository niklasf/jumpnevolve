package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
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
public abstract class EnemyTemplate extends AbstractObject {

	public EnemyTemplate(World world, Shape shape, float mass, boolean blockable) {
		super(world, shape, mass, blockable, false, true, false, true);
	}

	public void onLivingCrash(AbstractObject other) {
		// Anderes Objekt töten, wenn es kein Enemay ist
		if (other instanceof EnemyTemplate == false) {
			other.kill(this);
		}
	}
}
