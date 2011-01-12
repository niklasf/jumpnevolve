package com.googlecode.jumpnevolve.game.objects;

import com.googlecode.jumpnevolve.graphics.world.Jumping;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * 
 * Beschreibung: Ein Soldat der springt
 * 
 * Spezifikationen: siehe Soldier
 * 
 * Bewegungen: siehe Soldier; springt, wenn er den Boden berührt
 * 
 * Aggressivitäten: siehe Soldier
 * 
 * Immunitäten: siehe Soldier
 * 
 * Aktivierung: keine
 * 
 * Deaktivierung: keine
 * 
 * Besonderheiten: keine
 * 
 * @author Erik Wagner
 * 
 */
public class JumpingSoldier extends Soldier implements Jumping {

	private static final long serialVersionUID = -753627347562694811L;

	public JumpingSoldier(World world, Vector position) {
		super(world, position);
	}

	@Override
	public float getJumpingHeight() {
		return 40.0f;
	}
}
