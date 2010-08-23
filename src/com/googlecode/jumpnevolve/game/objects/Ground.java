package com.googlecode.jumpnevolve.game.objects;

import com.googlecode.jumpnevolve.game.GroundTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

/**
 * Normaler Boden, der so benutzt werden oder modifizeirt werden kann.
 * 
 * @author Erik Wagner
 * 
 */
public class Ground extends GroundTemplate {

	public Ground(World world, Shape shape) {
		super(world, shape);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void specialSettingsPerRound() {
		// Nichts tun
	}

	public void onActivableCrash(AbstractObject other) {
		// Boden kann nichts aktivieren
	}

	public void onPushableCrash(AbstractObject other) {
		// Boden kann nichts schieben
	}
}
