package com.googlecode.jumpnevolve.graphics.world;

/**
 * @author Erik Wagner
 * 
 */
public interface Jumping extends SimpleObject {

	public static final float JUMP_SOLDIER = 100;

	/**
	 * Gibt die Höhe des Sprungs zurück
	 * 
	 * @return >0.0f
	 */
	public float getJumpingHeight();
}
